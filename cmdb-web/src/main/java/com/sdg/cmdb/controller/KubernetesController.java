package com.sdg.cmdb.controller;


import com.sdg.cmdb.domain.HttpResult;
import com.sdg.cmdb.domain.kubernetes.KubernetesClusterVO;
import com.sdg.cmdb.domain.kubernetes.KubernetesServiceVO;
import com.sdg.cmdb.service.KubernetesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/kubernetes")
public class KubernetesController {

    @Autowired
    private KubernetesService kubernetesService;

    @RequestMapping(value = "/sync", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult sync() {
        return new HttpResult(kubernetesService.sync());
    }

    @RequestMapping(value = "/cluster/query", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult queryClusterPage() {
        return new HttpResult(kubernetesService.queryClusterPage());
    }


    @RequestMapping(value = "/cluster/syncLabel", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult syncLabelCluster(@RequestParam String clusterName) {
        return new HttpResult(kubernetesService.syncClusterLabel(clusterName));
    }



    @RequestMapping(value = "/cluster/save", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult saveClusterPage(@RequestBody KubernetesClusterVO kubernetesClusterVO) {
        return new HttpResult(kubernetesService.saveCluster(kubernetesClusterVO));
    }

    @RequestMapping(value = "/namespace/query", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult queryNamespacePage() {
        return new HttpResult(kubernetesService.queryNamespacePage());
    }

    @RequestMapping(value = "/service/query", method = RequestMethod.GET)
    @ResponseBody
    public HttpResult queryServicePage(@RequestParam long namespaceId,@RequestParam String name, @RequestParam String portName, @RequestParam int page, @RequestParam int length) {
        return new HttpResult(kubernetesService.queryServicePage(namespaceId, name, portName, page, length));
    }

    @RequestMapping(value = "/service/save", method = RequestMethod.POST)
    @ResponseBody
    public HttpResult saveService(@RequestBody KubernetesServiceVO kubernetesServiceVO) {
        return new HttpResult(kubernetesService.saveService(kubernetesServiceVO));
    }

    @RequestMapping(value = "/service/del", method = RequestMethod.DELETE)
    @ResponseBody
    public HttpResult delService(@RequestParam long id) {
        return new HttpResult(kubernetesService.delService(id));
    }
}
