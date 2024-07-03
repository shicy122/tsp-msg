package com.hycan.idn.tsp.message.facade;

import cn.hutool.core.io.IoUtil;
import com.hycan.idn.tsp.common.storage.StorageService;
import com.hycan.idn.tsp.common.storage.props.StorageProperties;
import com.hycan.idn.tsp.message.config.EndpointConfig;
import com.hycan.idn.tsp.message.constant.CommonConstants;
import com.hycan.idn.tsp.message.constant.ResourceTypeConstants;
import com.hycan.idn.tsp.message.exception.MsgBusinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;

/**
 * TODO 文件描述
 *
 * @author Shadow
 * @datetime 2024-03-01 09:02
 */
@Slf4j
@Component
public class ObsRemoteFacade {

    private final StorageService storageService;

    private final EndpointConfig endpointConfig;

    private String obsBasePath;

    public ObsRemoteFacade(StorageService storageService,
                           EndpointConfig endpointConfig,
                           StorageProperties storageProperties) {
        this.storageService = storageService;
        this.endpointConfig = endpointConfig;

        String defaultPlatform = storageProperties.getDefaultPlatform();
        List<StorageProperties.HuaweiObs> hwObsStorageList = storageProperties.getHuaweiObs();
        if (CollectionUtils.isEmpty(hwObsStorageList)) {
            return;
        }
        for (StorageProperties.HuaweiObs huaweiObs : hwObsStorageList) {
            if (defaultPlatform.equals(huaweiObs.getPlatform())) {
                this.obsBasePath = huaweiObs.getBasePath();
                break;
            }
        }
    }

    /**
     * 上传图文到OBS
     *
     * @param filePath 资源名称
     * @param content  资源内容
     */
    public String uploadFileToObs(String filePath, String content) {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(
                content.getBytes(StandardCharsets.UTF_8))) {
            storageService.upload(filePath, inputStream);

            return String.format(CommonConstants.ARTICLE_URL, endpointConfig.getTspMsgHost(),
                    ResourceTypeConstants.ARTICLE.toLowerCase(Locale.US), getEncoderUrlParams(filePath));
        } catch (Exception e) {
            throw new MsgBusinessException("上传图文失败", com.hycan.idn.tsp.common.core.constant.CommonConstants.FAIL, e.getMessage());
        }
    }

    /**
     * 上传图片、视频等文件到OBS
     *
     * @param filePath 资源名称
     * @param file     资源文件
     */
    public String uploadFileToObs(String filePath, MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            storageService.upload(filePath, inputStream);

            return endpointConfig.getCdn() + CommonConstants.FILE_JOINT + obsBasePath + filePath;
        } catch (Exception e) {
            throw new MsgBusinessException("上传文件失败", com.hycan.idn.tsp.common.core.constant.CommonConstants.FAIL, e.getMessage());
        }
    }

    /**
     * 从OBS下载资源
     *
     * @param resourceType 资源类型
     * @param resourceUrl  资源URL
     */
    public String downloadFileFromObs(String resourceType, String resourceUrl) {
        String filePath;
        if (ResourceTypeConstants.ARTICLE.equals(resourceType)) {
            filePath = getDecodeUrlParams(resourceUrl.substring(resourceUrl.lastIndexOf('=') + 1));
        } else {
            filePath = resourceUrl.substring((endpointConfig.getCdn() + CommonConstants.FILE_JOINT + obsBasePath).length() - 1);
        }

        try (InputStream inputStream = storageService.download(filePath)) {
            return IoUtil.read(inputStream, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new MsgBusinessException("资源下载失败", com.hycan.idn.tsp.common.core.constant.CommonConstants.FAIL, e.getMessage());
        }
    }

    /**
     * 从OBS删除资源
     *
     * @param resourceType 资源类型
     * @param resourceUrl  资源URL
     */
    public void deleteFileFromObs(String resourceType, String resourceUrl) {
        String filePath;
        if (ResourceTypeConstants.ARTICLE.equals(resourceType)) {
            filePath = getDecodeUrlParams(resourceUrl.substring(resourceUrl.lastIndexOf('=') + 1));
        } else {
            filePath = resourceUrl.substring((endpointConfig.getCdn() + CommonConstants.FILE_JOINT + obsBasePath).length() - 1);
        }

        storageService.delete(filePath);
    }


    /**
     * 获取上传OBS文件路径
     * 格式：${obsPathPrefix}/resource/${resourceType}/${resourceName}_${timestamp}.${suffix}
     * 样例：hycan/tsp/test/msg/resource/article/图文资源名称_1709187282862.ftl
     *
     * @param resourceType 文件类型
     * @param resourceName 文件名称
     * @param suffix       文件后缀名
     * @return 资源文件完整路径
     */
    public String getObsFilePath(String resourceType, String resourceName, String suffix) {
        return endpointConfig.getObsPathPrefix()
                + CommonConstants.RESOURCE
                + resourceType.toLowerCase(Locale.US)
                + CommonConstants.FILE_JOINT
                + resourceName
                + CommonConstants.UNDERLINE
                + System.currentTimeMillis()
                + suffix;
    }

    private String getEncoderUrlParams(String remoteName) {
        try {
            return URLEncoder.encode(remoteName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new MsgBusinessException("资源名字存在特殊符号无法解析", com.hycan.idn.tsp.common.core.constant.CommonConstants.FAIL, e.getMessage());
        }
    }

    private String getDecodeUrlParams(String resourceUrl) {
        try {
            return URLDecoder.decode(resourceUrl, "UTF-8");
        } catch (Exception e) {
            throw new MsgBusinessException("资源名字存在特殊符号无法解析", com.hycan.idn.tsp.common.core.constant.CommonConstants.FAIL, e.getMessage());
        }
    }
}
