package com.baiyi.opscloud.decorator.it;

import com.baiyi.opscloud.common.config.CachingConfig;
import com.baiyi.opscloud.domain.generator.opscloud.OcItAssetName;
import com.baiyi.opscloud.domain.generator.opscloud.OcItAssetType;
import com.baiyi.opscloud.domain.vo.tree.TreeVO;
import com.baiyi.opscloud.service.it.OcItAssetNameService;
import com.baiyi.opscloud.service.it.OcItAssetTypeService;
import com.google.common.collect.Lists;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/11/20 4:16 下午
 * @Since 1.0
 */

@Component("ItAssetTypeTreeDecorator")
public class ItAssetTypeTreeDecorator {

    @Resource
    private OcItAssetTypeService ocItAssetTypeService;

    @Resource
    private OcItAssetNameService ocItAssetNameService;

    @CacheEvict(cacheNames = CachingConfig.CACHE_NAME_COMMON_BY_DAY, key = "'ItAssetTypeTreeDecorator'", beforeInvocation = true)
    public void evictPreview() {
    }

    @Cacheable(cacheNames = CachingConfig.CACHE_NAME_COMMON_BY_DAY, key = "'ItAssetTypeTreeDecorator'")
    public List<TreeVO.Tree> getAssetTypeTree() {
        List<OcItAssetType> typeList = ocItAssetTypeService.queryOcItAssetTypeAll();
        List<TreeVO.Tree> treeList = Lists.newArrayListWithCapacity(typeList.size());
        typeList.forEach(ocItAssetType -> treeList.add(buildTree(ocItAssetType)));
        return treeList;
    }

    public TreeVO.Tree buildTree(OcItAssetType ocItAssetType) {
        List<OcItAssetName> nameList = ocItAssetNameService.queryOcItAssetNameByType(ocItAssetType.getId());
        List<TreeVO.Tree> treeList = Lists.newArrayListWithCapacity(nameList.size());
        nameList.forEach(ocItAssetName -> treeList.add(buildTree(ocItAssetName)));
        return TreeVO.Tree.builder()
                .label(ocItAssetType.getAssetType())
                .value(ocItAssetType.getId())
                .children(treeList)
                .build();
    }

    private TreeVO.Tree buildTree(OcItAssetName ocItAssetName) {
        return TreeVO.Tree.builder()
                .label(ocItAssetName.getAssetName())
                .value(ocItAssetName.getId())
                .build();
    }
}
