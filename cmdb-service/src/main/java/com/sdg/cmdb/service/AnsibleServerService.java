package com.sdg.cmdb.service;


public interface AnsibleServerService {

    String call(boolean isSudo, String hostPattern, String inventoryFile, String moduleName, String moduleArgs);

}
