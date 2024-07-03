package com.hycan.idn.tsp.message.utils;

import com.google.common.collect.Sets;
import com.hycan.idn.tsp.message.constant.ResourceFileConstants;
import com.hycan.idn.tsp.message.constant.ResourceTypeConstants;
import com.hycan.idn.tsp.message.exception.MsgBusinessException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

/**
 * TODO 文件描述
 *
 * @author Shadow
 * @datetime 2024-03-04 10:45
 */
public class FileUtil {

    /**
     * png图像
     */
    private static final String IMAGE_PNG = "image/png";

    /**
     * jpg图像
     */
    private static final String IMAGE_JPG = "image/jpg";

    /**
     * jpeg图像
     */
    private static final String IMAGE_JPEG = "image/jpeg";

    /**
     * mp4视频
     */
    private static final String VIDEO_MP4 = "video/mp4";

    /**
     * 视频avi
     */
    private static final String VIDEO_AVI = "video/avi";

    /**
     * 视频m4v
     */
    private static final String VIDEO_M4V = "video/m4v";

    /**
     * 图片扩展
     */
    private static final Set<String> IMAGE_EXTENSION = Sets.newHashSet("jpg", "jpeg", "png", "JPG", "JPEG", "PNG");

    /**
     * 视频扩展
     */
    private static final Set<String> VIDEO_EXTENSION = Sets.newHashSet("mp4", "avi", "m4v", "MP4", "AVI", "M4V");

    public static void validResourceFile(MultipartFile file, String resourceType) {
        if (StringUtils.isBlank(file.getOriginalFilename()) || file.getOriginalFilename().split("\\.").length < 2) {
            throw new MsgBusinessException("无效的文件名称!");
        }

        String fileExtension = getExtension(file.getContentType());
        if (ResourceTypeConstants.VIDEO.equals(resourceType)) {
            if (!VIDEO_EXTENSION.contains(fileExtension)) {
                throw new MsgBusinessException("无效的文件类型, 仅支持mp4、api、m4v格式!");
            }
        } else if (ResourceTypeConstants.PICTURE.equals(resourceType) ||
                ResourceTypeConstants.WALLPAPER.equals(resourceType) ||
                ResourceTypeConstants.COVER.equals(resourceType)) {
            if (!IMAGE_EXTENSION.contains(fileExtension)) {
                throw new MsgBusinessException("无效的文件类型, 仅支持jpg、jpeg、png格式!");
            } else if (file.getSize() > ResourceFileConstants.IMAGE_MAX_SIZE) {
                throw new MsgBusinessException("图片或壁纸文件大小必须小于5M!");
            }
        } else if(ResourceTypeConstants.AUDIO.equals(resourceType)) {
            //音频不进行验证
        }else {
            throw new MsgBusinessException("无效的资源类型!");
        }
    }

    /**
     * 得到扩展
     *
     * @param fileContentType 后缀
     * @return {@link String}
     */
    private static String getExtension(String fileContentType) {
        if (StringUtils.isBlank(fileContentType)) {
            return "";
        }

        switch (fileContentType) {
            case IMAGE_PNG:
                return "png";
            case IMAGE_JPG:
                return "jpg";
            case IMAGE_JPEG:
                return "jpeg";
            case VIDEO_MP4:
                return "mp4";
            case VIDEO_AVI:
                return "avi";
            case VIDEO_M4V:
                return "m4v";
            default:
                return "";
        }
    }

    /**
     * 获取文件名称
     *
     * @param file 文件
     * @return {@link String}
     */
    public static String getFileName(MultipartFile file) {
        if (StringUtils.isBlank(file.getOriginalFilename())) {
            return null;
        }
        final int indexOf = file.getOriginalFilename().lastIndexOf('.');
        return file.getOriginalFilename().substring(0, indexOf);
    }

    /**
     * 获取文件后缀
     *
     * @param file 文件
     * @return {@link String}
     */
    public static String getFileSuffix(MultipartFile file) {
        if (StringUtils.isBlank(file.getOriginalFilename())) {
            return null;
        }
        final int indexOf = file.getOriginalFilename().lastIndexOf('.');
        return file.getOriginalFilename().substring(indexOf);
    }
}
