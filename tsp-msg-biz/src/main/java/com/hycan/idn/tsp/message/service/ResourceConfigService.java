package com.hycan.idn.tsp.message.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hycan.idn.tsp.message.constant.BizDataStatusConstants;
import com.hycan.idn.tsp.message.constant.ResourceFileConstants;
import com.hycan.idn.tsp.message.constant.ResourceTypeConstants;
import com.hycan.idn.tsp.message.pojo.PageRspVO;
import com.hycan.idn.tsp.message.pojo.resourceconfig.CreateResourceConfigReqVO;
import com.hycan.idn.tsp.message.pojo.resourceconfig.ResourceConfigRspVO;
import com.hycan.idn.tsp.message.pojo.resourceconfig.PageResourceConfigReqVO;
import com.hycan.idn.tsp.message.pojo.resourceconfig.UpdateResourceConfigReqVO;
import com.hycan.idn.tsp.message.entity.mysql.ResourceConfigEntity;
import com.hycan.idn.tsp.message.exception.MsgBusinessException;
import com.hycan.idn.tsp.message.facade.ObsRemoteFacade;
import com.hycan.idn.tsp.message.repository.mysql.SceneCardTaskMapper;
import com.hycan.idn.tsp.message.repository.mysql.ResourceConfigMapper;
import com.hycan.idn.tsp.message.utils.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * 资源业务处理
 *
 * @author liangliang
 * @datetime 2022/09/30 14:18
 */
@Slf4j
@Component
public class ResourceConfigService {

    @Resource
    private ResourceConfigMapper resourceConfigMapper;

    @Resource
    private ObsRemoteFacade obsRemoteFacade;

    @Resource
    private SceneCardTaskMapper sceneCardTaskMapper;

    /**
     * 新增资源配置
     *
     * @param reqVO 新增资源配置请求VO
     * @return 资源配置响应VO
     */
    @Transactional(rollbackFor = Exception.class)
    public ResourceConfigRspVO createResourceConfig(CreateResourceConfigReqVO reqVO) {
        String resourceName = reqVO.getResourceName();
        String resourceType = reqVO.getResourceType();

        if (resourceConfigMapper.isExists(resourceName, resourceType)) {
            throw new MsgBusinessException("该资源类型下存在重复的资源配置, 请勿重复添加!");
        }

        // 图文消息处理，上传图文文件到OBS，并获取TSP渲染图文内容URL路径
        if (ResourceTypeConstants.ARTICLE.equals(resourceType)) {
            String resourceUrl = obsRemoteFacade.uploadFileToObs(
                    obsRemoteFacade.getObsFilePath(ResourceTypeConstants.ARTICLE, resourceName, ResourceFileConstants.FTL),
                    reqVO.getArticleContent());
            reqVO.setResourceUrl(resourceUrl);
        }

        ResourceConfigEntity entity = ResourceConfigEntity.of(reqVO);
        if (resourceConfigMapper.insert(entity) <= 0) {
            throw new MsgBusinessException("操作失败, 请稍后重试!");
        }

        return ResourceConfigRspVO.of(entity);
    }

    /**
     * 修改资源配置
     *
     * @param reqVO 资源配置
     * @return 结果
     */
    @Transactional(rollbackFor = Exception.class)
    public ResourceConfigRspVO updateResourceConfig(Long id, UpdateResourceConfigReqVO reqVO) {
        ResourceConfigEntity entity = resourceConfigMapper.selectById(id);
        if (null == entity) {
            throw new MsgBusinessException("资源配置不存在!");
        }

        if (sceneCardTaskMapper.isResourceConfigRelationSendingTask(id)) {
            throw new MsgBusinessException("该资源配置所关联的场景卡片任务, 当前为推送中状态, 不允许修改!");
        }

        if (!reqVO.getResourceName().equals(entity.getResourceName())) {
            if (resourceConfigMapper.isExists(reqVO.getResourceName(), reqVO.getResourceType())) {
                throw new MsgBusinessException("该资源类型下存在重复的资源配置, 请勿重复操作!");
            }
        }

        if (!reqVO.getResourceType().equals(entity.getResourceType())) {
            throw new MsgBusinessException("资源类型不允许变更!");
        }

        // 图文消息处理(上传新的图文，删除旧的图文)，上传图文文件到OBS，并获取TSP渲染图文内容URL路径
        if (ResourceTypeConstants.ARTICLE.equals(reqVO.getResourceType())) {
            String resourceUrl = obsRemoteFacade.uploadFileToObs(
                    obsRemoteFacade.getObsFilePath(ResourceTypeConstants.ARTICLE, reqVO.getResourceName(), ResourceFileConstants.FTL),
                    reqVO.getArticleContent());

            obsRemoteFacade.deleteFileFromObs(ResourceTypeConstants.ARTICLE, entity.getResourceUrl());

            reqVO.setResourceUrl(resourceUrl);
        }

        ResourceConfigEntity.of(entity, reqVO.getResourceName(), reqVO.getResourceSize(), reqVO.getResourceUrl());
        if (resourceConfigMapper.updateById(entity) <= 0) {
            throw new MsgBusinessException("操作失败, 请稍后重试!");
        }

        return ResourceConfigRspVO.of(entity);
    }

    /**
     * 更新资源状态
     *
     * @param id     资源配置ID
     * @param status 资源配置状态
     * @return {@link ResourceConfigRspVO}
     */
    @Transactional(rollbackFor = Exception.class)
    public ResourceConfigRspVO updateResourceConfigStatus(Long id, String status) {
        if (BizDataStatusConstants.FORBIDDEN.equals(status)) {
            if (sceneCardTaskMapper.isResourceConfigRelationSendingTask(id)) {
                throw new MsgBusinessException("该资源配置所关联的场景卡片任务, 当前为推送中状态, 不允许修改!");
            }
        }

        if (resourceConfigMapper.updateStatusById(id, status) <= 0) {
            throw new MsgBusinessException("操作失败, 请稍后重试!");
        }
        return ResourceConfigRspVO.of(resourceConfigMapper.selectById(id));
    }

    /**
     * 删除资源配置信息
     *
     * @param id 资源配置主键
     * @return 操作结果
     */
    @Transactional(rollbackFor = Exception.class)
    public int deleteResourceConfig(Long id) {
        ResourceConfigEntity entity = resourceConfigMapper.selectById(id);
        if (null == entity) {
            throw new MsgBusinessException("资源配置不存在!");
        }

        if (sceneCardTaskMapper.isResourceConfigRelationTask(id)
        ) {
            throw new MsgBusinessException("该资源配置已关联场景卡片任务, 请先删除场景卡片任务后, 再删除资源配置!");
        }

        obsRemoteFacade.deleteFileFromObs(entity.getResourceType(), entity.getResourceUrl());

        return resourceConfigMapper.deleteById(id);
    }

    /**
     * 查询资源配置
     *
     * @param id 资源配置主键
     * @return 资源配置
     */
    public ResourceConfigRspVO showResourceConfig(Long id) {
        ResourceConfigEntity entity = resourceConfigMapper.selectById(id);
        if (null == resourceConfigMapper.selectById(id)) {
            throw new MsgBusinessException("资源配置不存在!");
        }

        String articleContent = null;
        if (ResourceTypeConstants.ARTICLE.equals(entity.getResourceType())) {
            articleContent = obsRemoteFacade.downloadFileFromObs(ResourceTypeConstants.ARTICLE, entity.getResourceUrl());
        }

        return ResourceConfigRspVO.of(entity, articleContent);
    }

    /**
     * 按条件分页查询资源配置
     *
     * @param reqVO 分页查询资源配置请求VO
     * @return 分页返回资源配置响应VO
     */
    public PageRspVO<ResourceConfigRspVO> selectResourcePage(PageResourceConfigReqVO reqVO) {
        IPage<ResourceConfigRspVO> page = resourceConfigMapper.findPage(Page.of(reqVO.getCurrent(), reqVO.getSize()), reqVO);
        return PageRspVO.of(page);
    }

    /**
     * 上传资源文件到OBS
     *
     * @param file 文件
     * @return {@link String}
     */
    public String uploadResourceToObs(MultipartFile file, String resourceType) {
        if (file == null || file.isEmpty()) {
            throw new MsgBusinessException("请选择需要上传的文件!");
        }

        // 检验文件格式
        FileUtil.validResourceFile(file, resourceType);

        String obsFilePath = obsRemoteFacade.getObsFilePath(resourceType, FileUtil.getFileName(file), FileUtil.getFileSuffix(file));

        return obsRemoteFacade.uploadFileToObs(obsFilePath, file);
    }
}
