package com.bornium.infrastructurebootstrapping.base.controller.objectmanagement;

import com.bornium.infrastructurebootstrapping.provisioning.entities.cloud.Cloud;
import com.bornium.infrastructurebootstrapping.provisioning.repositories.CloudRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/clouds")
public class CloudController extends ObjectController<Cloud, CloudRepository> {
}
