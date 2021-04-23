package com.baiyi.opscloud.it;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.config.OpscloudConfig;
import com.baiyi.opscloud.facade.it.ItAssetFacade;
import com.baiyi.opscloud.service.it.OcItAssetNameService;
import com.baiyi.opscloud.service.it.OcItAssetService;
import com.baiyi.opscloud.service.org.OcOrgDepartmentMemberService;
import com.baiyi.opscloud.service.user.OcUserService;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/1/5 3:35 下午
 * @Since 1.0
 */
@Slf4j
class ItAssetTest extends BaseUnit {

    @Resource
    private OpscloudConfig opscloudConfig;

    @Resource
    private OcItAssetNameService ocItAssetNameService;

    @Resource
    private OcItAssetService ocItAssetService;

    @Resource
    private ItAssetFacade itAssetFacade;

    @Resource
    private OcUserService ocUserService;

    @Resource
    private OcOrgDepartmentMemberService ocOrgDepartmentMemberService;

//    @Test
//    void importAssetTest() {
//        String path = Joiner.on("/").join(opscloudConfig.getDataPath(), "it-asset-8.csv");
//        try {
//            FileInputStream fileInputStream = new FileInputStream(path);
//            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
//            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//            CSVParser parser = CSVFormat.DEFAULT.parse(bufferedReader);
//            int rowIndex = 0;
//            for (CSVRecord record : parser.getRecords()) {
//                if (rowIndex == 0) {
//                    rowIndex++;
//                    continue;
//                }
//                OcItAsset ocItAsset = new OcItAsset();
//                String company = record.get(10).trim();
//                if (company.equals("杭州辛橙")) {
//                    ocItAsset.setAssetCompany(1);
//                }
//                if (company.equals("小租电脑") || company.equals("小租网络")) {
//                    ocItAsset.setAssetCompany(2);
//                }
//
//                ocItAsset.setAssetCode(record.get(1).trim());
//                OcItAssetName ocItAssetName = ocItAssetNameService.queryOcItAssetNameByName(record.get(4).trim());
//                ocItAsset.setAssetNameId(ocItAssetName.getId());
////                String status = record.get(2);
////                if (status.equals("空闲")) {
//                ocItAsset.setAssetStatus(ItAssetStatus.FREE.getType());
////                }
////                if (status.equals("在用")) {
////                    ocItAsset.setAssetStatus(ItAssetStatus.USED.getType());
////                }
////                if (status.equals("借用")) {
////                    ocItAsset.setAssetStatus(ItAssetStatus.BORROW.getType());
////                }
////                if (status.equals("处置")) {
////                    ocItAsset.setAssetStatus(ItAssetStatus.DISPOSE.getType());
////                }
//                ocItAsset.setAssetConfiguration(record.get(5).trim());
//                ocItAsset.setAssetPrice(record.get(8).trim());
//                String date = record.get(7).trim();
//                if (date.equals("2019")) {
//                    ocItAsset.setAssetAddTime(new Date(1569859200000L));
//                }
//                if (date.equals("2020.1") || date.equals("2020")) {
//                    ocItAsset.setAssetAddTime(new Date(1577808000000L));
//                }
//                if (date.equals("2020.2")) {
//                    ocItAsset.setAssetAddTime(new Date(1580486400000L));
//                }
//                if (date.equals("2020.3")) {
//                    ocItAsset.setAssetAddTime(new Date(1582992000000L));
//                }
//                if (date.equals("2020.4")) {
//                    ocItAsset.setAssetAddTime(new Date(1585670400000L));
//                }
//                if (date.equals("2020.5")) {
//                    ocItAsset.setAssetAddTime(new Date(1588262400000L));
//                }
//                if (date.equals("2020.6")) {
//                    ocItAsset.setAssetAddTime(new Date(1590940800000L));
//                }
//                if (date.equals("2020.7")) {
//                    ocItAsset.setAssetAddTime(new Date(1593532800000L));
//                }
//                if (date.equals("2020.8")) {
//                    ocItAsset.setAssetAddTime(new Date(1596211200000L));
//                }
//                if (date.equals("2020.9")) {
//                    ocItAsset.setAssetAddTime(new Date(1598889600000L));
//                }
//                if (date.equals("2020.10")) {
//                    ocItAsset.setAssetAddTime(new Date(1601481600000L));
//                }
//                if (date.equals("2020.11")) {
//                    ocItAsset.setAssetAddTime(new Date(1604160000000L));
//                }
//                if (date.equals("2020.12")) {
//                    ocItAsset.setAssetAddTime(new Date(1606752000000L));
//                }
////                ocItAsset.setUseTime(TimeUtils.acqGmtDateV2(record.get(14)));
//                ocItAsset.setAssetPlace(record.get(15).trim());
//                ocItAsset.setRemark(record.get(16).trim());
//                try {
//                    ocItAssetService.addOcItAsset(ocItAsset);
//                } catch (Exception e) {
//                    log.error("插入失败，ocItAssetCode:{}", ocItAsset.getAssetCode(), e);
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    void importAssetApplyTest() {
//        String path = Joiner.on("/").join(opscloudConfig.getDataPath(), "it-asset-apply.csv");
//        try {
//            FileInputStream fileInputStream = new FileInputStream(path);
//            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
//            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//            CSVParser parser = CSVFormat.DEFAULT.parse(bufferedReader);
//            for (CSVRecord record : parser.getRecords()) {
//                OcItAsset ocItAsset = ocItAssetService.queryOcItAssetByAssetCode(record.get(0).trim());
//                if (ocItAsset == null) {
//                    log.error(record.get(0).trim() + " 资产不存在");
//                    continue;
//                }
//                String status = record.get(1).trim();
//                if (status.equals(ItAssetStatus.USED.getDesc())) {
//                    List<OcUser> ocUser = ocUserService.queryOcUserByDisplayName(record.get(2).trim());
//                    if (CollectionUtils.isEmpty(ocUser)) {
//                        log.error(record.get(2).trim() + " 用户不存在");
//                        continue;
//                    } else {
//                        ItAssetParam.ApplyAsset applyAsset = new ItAssetParam.ApplyAsset();
//                        applyAsset.setAssetId(ocItAsset.getId());
//                        applyAsset.setApplyTime(TimeUtils.acqGmtDateV2(record.get(3)).getTime());
//                        applyAsset.setApplyType(ItAssetApplyType.USE.getType());
//                        applyAsset.setUserId(ocUser.get(0).getId());
//                        List<OcOrgDepartmentMember> memberList = ocOrgDepartmentMemberService.queryOcOrgDepartmentMemberByUserId(ocUser.get(0).getId());
//                        if (!CollectionUtils.isEmpty(memberList)) {
//                            Integer departmentId = memberList.get(0).getDepartmentId();
//                            applyAsset.setUserOrgDeptId(departmentId);
//                        }
//                        try {
//                            itAssetFacade.applyAsset(applyAsset);
//                        }catch (Exception e){
//                        }
//                    }
//                }
//                if (status.equals(ItAssetStatus.DISPOSE.getDesc())) {
//                    ItAssetParam.DisposeAsset disposeAsset = new ItAssetParam.DisposeAsset();
//                    disposeAsset.setAssetId(ocItAsset.getId());
//                    disposeAsset.setDisposeTime(TimeUtils.acqGmtDateV2(record.get(3)).getTime());
//                    disposeAsset.setDisposeType(3);
//                    disposeAsset.setRemark(record.get(4).trim());
//                    itAssetFacade.disposeAsset(disposeAsset);
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

}
