package com.sdg.cmdb.service.impl;

import com.sdg.cmdb.dao.cmdb.IPGroupDao;
import com.sdg.cmdb.dao.cmdb.IPServerGroupDao;
import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.ErrorCode;
import com.sdg.cmdb.domain.TableVO;
import com.sdg.cmdb.domain.ip.IPDetailDO;
import com.sdg.cmdb.domain.ip.IPGroupSearchVO;
import com.sdg.cmdb.domain.ip.IPNetworkDO;
import com.sdg.cmdb.service.IPGroupService;
import com.sdg.cmdb.service.IPService;
import com.sdg.cmdb.util.IPUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by zxxiao on 16/9/9.
 */
@Service
public class IPGroupServiceImpl implements IPGroupService {

    private static final Logger logger = LoggerFactory.getLogger(IPGroupServiceImpl.class);

    /**
     * 默认未生成ip标记
     */
    private final static String NO_RECORD = "-1";

    @Resource
    private IPService ipService;

    @Resource
    private IPGroupDao ipGroupDao;

    @Resource
    private IPServerGroupDao ipServerGroupDao;

    @Override
    public TableVO<List<IPNetworkDO>> getIPGroupPage(IPGroupSearchVO searchVO, int page, int length) {
        long size = ipGroupDao.queryIpGroupSize(searchVO);
        List<IPNetworkDO> list = ipGroupDao.queryIpGroupPage(searchVO, page * length, length);
        return new TableVO<>(size, list);
    }

    @Override
    public BusinessWrapper<Boolean> saveIPGroupInfo(IPNetworkDO ipNetworkDO) {
        try {
            if (ipNetworkDO.getId() == 0) {
                ipGroupDao.addIPGroup(ipNetworkDO);
            } else {
                ipGroupDao.updateIPGroup(ipNetworkDO);
            }
            return new BusinessWrapper<>(true);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new BusinessWrapper<>(ErrorCode.serverFailure);
        }
    }

    @Override
    public boolean delIPGroupInfo(long ipGroupId) {
        try {
            ipGroupDao.delIPGroup(ipGroupId);
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return false;
        }
    }

    @Override
    public BusinessWrapper createIp(long ipGroupId) {
        IPNetworkDO ipNetworkDO = ipGroupDao.queryIPGroupInfo(ipGroupId);

        String[] result = splitIpAndmaskBit(ipNetworkDO);
        String startIp = getBeginIp(result);
        String endIp = getEndIp(result);
        String[] produceMark = getProduceMark(ipNetworkDO);

        return produce(ipNetworkDO, startIp, endIp, produceMark);
    }

    @Override
    public IPNetworkDO queryIPGroupById(long ipGroupId) {
        return ipGroupDao.queryIPGroupInfo(ipGroupId);
    }

    @Override
    public TableVO<List<IPNetworkDO>> queryIPGroupByServerGroupId(long serverGroupId, int page, int length) {
        long size = ipServerGroupDao.queryIPGroupByServerGroupSize(serverGroupId);
        List<IPNetworkDO> data = ipServerGroupDao.queryIPGroupByServerGroupPage(serverGroupId, page * length, length);

        return new TableVO<>(size, data);
    }

    /**
     * 分割出ip和掩码
     *
     * @param ipNetworkDO
     * @return
     */
    private String[] splitIpAndmaskBit(IPNetworkDO ipNetworkDO) {
        String ipnetwork = ipNetworkDO.getIpNetwork();
        if (null != ipnetwork && !"".equals(ipnetwork)) {
            String[] result = ipnetwork.split("\\/");
            return result;
        }
        return null;
    }

    /**
     * 获取ip
     *
     * @param result
     * @return
     */
    private String getIp(String[] result) {
        if (null != result) {
            return result[0];
        }
        return null;
    }

    /**
     * 获取掩码
     *
     * @param result
     * @return
     */
    private String getmaskBit(String[] result) {
        if (null != result && result.length > 1) {
            return result[1];
        }
        return null;
    }

    /**
     * 获得初始ip
     *
     * @param ipAndBit
     * @return
     */
    private String getBeginIp(String[] ipAndBit) {
        String ip = getIp(ipAndBit);
        String maskBit = getmaskBit(ipAndBit);
        if (null == ip || null == maskBit) {
            logger.info("生成ip过程,获得起始ip时候,ip或者掩码为空");
        }
        return IPUtils.getBeginIpStr(ip, maskBit);
    }

    /**
     * 获得结束ip
     *
     * @param ipAndBit
     * @return
     */
    private String getEndIp(String[] ipAndBit) {
        String ip = getIp(ipAndBit);
        String maskBit = getmaskBit(ipAndBit);
        if (null == ip || null == maskBit) {
            logger.info("生成ip过程,获得结束ip时候,ip或者掩码为空");
        }
        return IPUtils.getEndIpStr(ip, maskBit);
    }

    /**
     * 获得ip段ip的生成记录
     *
     * @param ipNetworkDO
     * @return
     */
    private String[] getProduceMark(IPNetworkDO ipNetworkDO) {
        if (null == ipNetworkDO) {
            logger.info("ip组为空");
            return null;
        }
        String mark = ipNetworkDO.getProduceMark();
        if (null == mark || "".equals(mark)) {
            logger.info("生成记录mark为空");
            return null;
        }
        return mark.split("\\/");
    }

    /**
     * 目前只考虑从C段开始生成
     *
     * @param startIp
     * @param endId
     * @param produceMark
     * @return
     */
    private BusinessWrapper<Integer> produce(IPNetworkDO ipNetwork, String startIp, String endId, String[] produceMark) {
        if (null == startIp || null == endId || null == produceMark) {
            return new BusinessWrapper<>(ErrorCode.serverFailure.getCode(), "produce ip 过程中参数为空");
        }
        try {
            List<IPDetailDO> ipDetails = createIpDetails(ipNetwork, startIp, endId, produceMark);
            return ipService.saveGroupIPs(ipDetails);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new BusinessWrapper<>(ErrorCode.serverFailure.getCode(), "生成ip过程出现异常.");
        }
    }

    /**
     * param ipNetwork
     *
     * @param startIp
     * @param endId
     * @param produceMark
     * @return
     */
    private List<IPDetailDO> createIpDetails(IPNetworkDO ipNetwork, String startIp, String endId, String[] produceMark) {
        Integer cSegmentValue = findCSegmentValue(startIp, endId, produceMark);
        if (null == cSegmentValue) {
            return Collections.emptyList();
        }
        Integer forStart = getStartIpNum(startIp, endId, produceMark);
        Integer forEnd = getEndIpNum(startIp, endId, produceMark);
        if (null == forStart || null == forEnd) {
            logger.info("生成ip过程中,for循环的开始或者结束值为空");
            return Collections.EMPTY_LIST;
        }
        String aSegmentIp = getSegment(startIp, 0);
        String bSegmentIp = getSegment(startIp, 1);
        List<IPDetailDO> ipDetails = new ArrayList<>();
        for (int i = forStart; i < forEnd; i++) {
            StringBuilder sb = new StringBuilder();
            sb.append(aSegmentIp);
            sb.append(".");
            sb.append(bSegmentIp);
            sb.append(".");
            sb.append(cSegmentValue);
            sb.append(".");
            sb.append(i);
            IPDetailDO ipDetail = new IPDetailDO(ipNetwork.getId(), sb.toString(), ipNetwork.getIpType());
            ipDetails.add(ipDetail);
        }
        updateIpNetworkRecord(ipNetwork, String.valueOf(cSegmentValue), String.valueOf(forEnd));
        return ipDetails;
    }

    /**
     * 找寻本次生成ipC段的值
     *
     * @param startIp
     * @param endId
     * @param produceMark
     * @return
     */
    private Integer findCSegmentValue(String startIp, String endId, String[] produceMark) {
        Integer cProduceMark = Integer.valueOf(getCSegmentRecord(produceMark));
        Integer cStart = Integer.valueOf(getSegment(startIp, 2));
        Integer cEnd = Integer.valueOf(getSegment(endId, 2));
        if (-1 == cProduceMark) {
            return cStart;
        }
        if ((cProduceMark + 1) <= cEnd) {
            return cProduceMark + 1;
        }
        return null;
    }

    /**
     * 获取C段记录
     *
     * @param produceRecord
     * @return
     */
    private String getCSegmentRecord(String[] produceRecord) {
        if (null != produceRecord && produceRecord.length > 2) {
            return produceRecord[2];
        }
        return null;
    }

    /**
     * 获得ip四段中指定段得值
     *
     * @param ip
     * @param index
     * @return
     */
    public static String getSegment(String ip, int index) {
        if (null != ip && !"".equals(ip)) {
            String[] segments = ip.split("\\.");
            if (null != segments && segments.length > index) {
                return segments[index];
            }
        }
        return null;
    }

    /**
     * 获得for循环结束值
     *
     * @param startIp
     * @param endId
     * @param produceMark
     * @return
     */
    private Integer getStartIpNum(String startIp, String endId, String[] produceMark) {
        Integer cStart = Integer.valueOf(getSegment(startIp, 2));
        Integer cSegmentValue = findCSegmentValue(startIp, endId, produceMark);
        if (null != cSegmentValue) {//找到本次该生成的C段值,本次允许生成
            if (cSegmentValue == cStart) {
                return Integer.valueOf(getSegment(startIp, 3));
            } else {
                return 0;
            }
        }
        return null;
    }

    /**
     * 获得for循环结束值
     *
     * @param startIp
     * @param endId
     * @param produceMark
     * @return
     */
    private Integer getEndIpNum(String startIp, String endId, String[] produceMark) {
        Integer cEnd = Integer.valueOf(getSegment(endId, 2));
        Integer cSegmentValue = findCSegmentValue(startIp, endId, produceMark);
        if (null != cSegmentValue) {//找到本次该生成的C段值,本次允许生成
            if (cSegmentValue < cEnd) {
                return 255;
            }
            if (cSegmentValue == cEnd) {
                return Integer.valueOf(getSegment(endId, 3));
            }
        }
        return null;
    }

    /**
     * 更新生成记录
     *
     * @param ipNetwork
     * @param cSegmentIp
     * @param dSegmentIp
     */
    private void updateIpNetworkRecord(IPNetworkDO ipNetwork, String cSegmentIp, String dSegmentIp) {
        StringBuilder sb = new StringBuilder();
        sb.append(NO_RECORD);
        sb.append("/");
        sb.append(NO_RECORD);
        sb.append("/");
        sb.append(cSegmentIp);
        sb.append("/");
        sb.append(dSegmentIp);
        ipNetwork.setProduceMark(sb.toString());

        ipGroupDao.updateIPGroup(ipNetwork);
    }
}
