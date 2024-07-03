//package com.hycan.idn.tsp.message.controller;
//
//import cn.hutool.core.bean.BeanUtil;
//import cn.hutool.core.util.StrUtil;
//import cn.hutool.json.JSONObject;
//import cn.hutool.json.JSONUtil;
//import com.hycan.idn.tsp.common.core.constant.CommonConstants;
//import com.hycan.idn.tsp.common.core.constant.SecurityConstants;
//import com.hycan.idn.tsp.common.core.util.R;
//import com.hycan.idn.tsp.message.constant.ResourceTypeConstants;
//import com.hycan.idn.tsp.message.pojo.resourceconfig.UpdateResourceConfigReqVO;
//import com.hycan.idn.tsp.message.entity.mysql.ResourceConfigEntity;
//import lombok.AllArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MvcResult;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//
//import javax.imageio.ImageIO;
//import java.awt.Color;
//import java.awt.Font;
//import java.awt.FontMetrics;
//import java.awt.Graphics;
//import java.awt.image.BufferedImage;
//import java.io.ByteArrayInputStream;
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
///**
// * 资源控制器测试
// *
// * @author liangliang
// * @date 2022/12/26
// */
//@ActiveProfiles("unit")
//@AutoConfigureMockMvc
//@ExtendWith(MockitoExtension.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
//@Slf4j
//@AllArgsConstructor
//public class ResourceConfigControllerTest {
//    /**
//     * 资源url
//     */
//    private static final String RESOURCE_URL = "/msg-center-svc/v1/resource-configs";
//    /**
//     * 编码
//     */
//    private static final String ENCODE = "UTF-8";
//    /**
//     * 资源大小
//     */
//    private static final long RESOURCE_SIZE = 512;
//    /**
//     * 宽度
//     */
//    private static final int WIDTH = 1166;
//    /**
//     * 高度
//     */
//    private static final int HEIGHT = 464;
//    /**
//     * 字体大小
//     */
//    private static final int FONT_SIZE = 22;
//    /**
//     * 宽度分裂
//     */
//    private static final int WIDTH_SPLIT = 2;
//    /**
//     * 宽度x分裂num
//     */
//    private static final int WIDTH_X_SPLIT_NUM = 35;
//    /**
//     * 宽度y分裂num
//     */
//    private static final int WIDTH_Y_SPLIT_NUM = 5;
//
//    /**
//     * 模拟mvc
//     */
//    @Autowired
//    private final MockMvc mockMvc;
//    /**
//     * 资源服务
//     */
//    @Autowired
//    private final ResourceService resourceService;
//
//    /**
//     * 用例描述：
//     * 测试资源配置列表接口成功，携带所有必填参数
//     * 预置条件：无
//     * @throws Exception
//     */
//    @Test
//    public void test_listResourceConfig_withAllParams_success() throws Exception {
//        final MvcResult result = mockMvc.perform(
//                MockMvcRequestBuilders.get(RESOURCE_URL)
//                        .header(SecurityConstants.FROM, SecurityConstants.FROM_IN)).andReturn();
//        result.getResponse().setCharacterEncoding(ENCODE);
//        final R r = JSONUtil.toBean(result.getResponse().getContentAsString(), R.class);
//        assertEquals(r.getCode(), CommonConstants.SUCCESS);
//    }
//    /**
//     * 用例描述：
//     * 测试资源配置新增接口成功，携带所有必填参数
//     * 预置条件：无
//     *
//     * @throws Exception
//     */
//    @Test
//    public void test_createResourceConfig_withAllParams_success() throws Exception {
//        final MvcResult result = mockMvc.perform(
//                MockMvcRequestBuilders.post(RESOURCE_URL)
//                        .contentType(MediaType.APPLICATION_JSON_VALUE)
//                        .content(JSONUtil.toJsonStr(buildResourceReq("create_"))))
//                .andReturn();
//        result.getResponse().setCharacterEncoding(ENCODE);
//        final R r = JSONUtil.toBean(result.getResponse().getContentAsString(), R.class);
//        assertEquals(r.getCode(), CommonConstants.SUCCESS);
//    }
//    /**
//     * 用例描述：
//     * 测试资源配置修改接口成功，携带所有必填参数
//     * 预置条件：无
//     *
//     * @throws Exception
//     */
//    @Test
//    public void test_updateResourceConfig_withAllParams_success() throws Exception {
//        final MvcResult result = mockMvc.perform(
//                MockMvcRequestBuilders
//                        .put(RESOURCE_URL + "/" + buildUpdateOrDeleteResourceReq("build_"))
//                        .contentType(MediaType.APPLICATION_JSON_VALUE)
//                        .content(JSONUtil.toJsonStr(buildResourceReq("update_"))))
//                .andReturn();
//        result.getResponse().setCharacterEncoding(ENCODE);
//        final R r = JSONUtil.toBean(result.getResponse().getContentAsString(), R.class);
//        assertEquals(r.getCode(), CommonConstants.SUCCESS);
//    }
//    /**
//     * 用例描述：
//     * 测试资源配置删除接口成功，携带所有必填参数
//     * 预置条件：无
//     *
//     * @throws Exception
//     */
//    @Test
//    public void test_deleteResourceConfig_withAllParams_success() throws Exception {
//        final MvcResult result = mockMvc.perform(
//                MockMvcRequestBuilders
//                        .delete(RESOURCE_URL + "/" + buildUpdateOrDeleteResourceReq("delete_")))
//                .andReturn();
//        result.getResponse().setCharacterEncoding(ENCODE);
//        final R r = JSONUtil.toBean(result.getResponse().getContentAsString(), R.class);
//        assertEquals(r.getCode(), CommonConstants.SUCCESS);
//    }
//    /**
//     * 用例描述：
//     * 测试资源上传接口成功，携带所有必填参数
//     * 预置条件：无
//     *
//     * @throws Exception
//     */
//    @Test
//    public void test_uploadResource_withAllParams_success() throws Exception {
//        final MvcResult result = mockMvc.perform(
//                MockMvcRequestBuilders.fileUpload(RESOURCE_URL + "/upload")
//                        .file(buildJpgFile()))
//                .andReturn();
//        result.getResponse().setCharacterEncoding(ENCODE);
//        final R r = JSONUtil.toBean(result.getResponse().getContentAsString(), R.class);
//        System.out.println(r.getData());
//        assertEquals(r.getCode(), CommonConstants.SUCCESS);
//    }
//    /**
//     * 用例描述：
//     * 测试资源图文渲染接口成功，携带所有必填参数
//     * 预置条件：无
//     *
//     * @throws Exception
//     */
//    @Test
//    public void test_article_withAllParams_success() throws Exception {
//        final MvcResult result = mockMvc.perform(
//                MockMvcRequestBuilders.get(RESOURCE_URL + "/detail?" + buildArticle())
//                )
//                .andReturn();
//        result.getResponse().setCharacterEncoding(ENCODE);
//        Assertions.assertTrue(StrUtil.isNotBlank(result.getResponse().getContentAsString()));
//    }
//
//    private UpdateResourceConfigReqVO buildResourceReq(String flag) {
//        final UpdateResourceConfigReqVO updateResourceConfigReqVO = new UpdateResourceConfigReqVO();
//        updateResourceConfigReqVO.setResourceName(flag + "mock测试资源");
//        updateResourceConfigReqVO.setResourceType(ResourceTypeConstants.VIDEO);
//        updateResourceConfigReqVO.setResourceSize(RESOURCE_SIZE);
//        updateResourceConfigReqVO.setResourceUrl("https://tsp-static-t.hycan.com.cn/hycan/tsp/msg/resource/picture/" +
//                "white clouds on blue sky_1665287065972.jpg");
//        return updateResourceConfigReqVO;
//    }
//
//    private String buildUpdateOrDeleteResourceReq(String flag) {
//        final ResourceConfigEntity msgResource = BeanUtil.toBean(buildResourceReq(flag), ResourceConfigEntity.class);
//        resourceService.save(msgResource);
//        return msgResource.getId().toString();
//    }
//
//    private MockMultipartFile buildJpgFile() throws IOException {
//        final BufferedImage bufferedImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
//        final Graphics g = bufferedImage.getGraphics();
//        g.setClip(0, 0, WIDTH, HEIGHT);
//        g.setColor(Color.white);
//        g.fillRect(0, 0, WIDTH, HEIGHT);
//        g.setColor(Color.black);
//        final Font font = new Font("宋体", Font.PLAIN, FONT_SIZE);
//        g.setFont(font);
//        final FontMetrics fm = g.getFontMetrics(font);
//        String title = "上传文件mock测试";
//        final int titleWidth = fm.stringWidth(title);
//        final int titleWidthX = (WIDTH - titleWidth) / WIDTH_SPLIT - WIDTH_X_SPLIT_NUM;
//        g.drawString(title, titleWidthX, HEIGHT /WIDTH_SPLIT - WIDTH_Y_SPLIT_NUM);
//        g.dispose();
//        final ByteArrayOutputStream os = new ByteArrayOutputStream();
//        ImageIO.write(bufferedImage, "jpg", os);
//        final InputStream input = new ByteArrayInputStream(os.toByteArray());
//        return new MockMultipartFile("file", "file.jpg", "image/jpg", input);
//    }
//
//    private String buildArticle() throws Exception {
//        final UpdateResourceConfigReqVO article = new UpdateResourceConfigReqVO();
//        article.setResourceName("createArticle_图文渲染测试");
//        article.setResourceType(ResourceTypeConstants.ARTICLE);
//        article.setArticleContent("mock图文渲染测试");
//        final MvcResult articleResult = mockMvc.perform(
//                MockMvcRequestBuilders.post(RESOURCE_URL)
//                        .contentType(MediaType.APPLICATION_JSON_VALUE)
//                        .content(JSONUtil.toJsonStr(article)))
//                .andReturn();
//        articleResult.getResponse().setCharacterEncoding(ENCODE);
//        final R r = JSONUtil.toBean(articleResult.getResponse().getContentAsString(), R.class);
//        final JSONObject data = (JSONObject)r.getData();
//        final String resourceUrl = data.get("resourceUrl").toString();
//        return resourceUrl.substring(resourceUrl.indexOf('?') + 1);
//    }
//}
