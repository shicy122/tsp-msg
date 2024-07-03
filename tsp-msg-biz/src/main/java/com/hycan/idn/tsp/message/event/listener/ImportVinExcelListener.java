package com.hycan.idn.tsp.message.event.listener;

import cn.hutool.core.text.CharSequenceUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.hycan.idn.tsp.message.pojo.basetask.ImportVinReqVO;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * vin批量导入监听器
 * @author liangliang
 */
public class ImportVinExcelListener extends AnalysisEventListener<ImportVinReqVO> {

	private List<ImportVinReqVO> dataList = new ArrayList<>();

	@Override
	public void invoke(ImportVinReqVO vinFileDTO, AnalysisContext analysisContext) {
		if (!ObjectUtils.isEmpty(vinFileDTO) && CharSequenceUtil.isNotBlank(vinFileDTO.getVin())) {
			ImportVinReqVO vo = new ImportVinReqVO();
			vo.setVin(vinFileDTO.getVin());
			dataList.add(vo);
		}
	}

	@Override
	public void doAfterAllAnalysed(AnalysisContext analysisContext) {

	}

	public List<ImportVinReqVO> getDataList() {
		return this.dataList;
	}

	public void setDataList(List<ImportVinReqVO> dataList) {
		this.dataList = dataList;
	}
}
