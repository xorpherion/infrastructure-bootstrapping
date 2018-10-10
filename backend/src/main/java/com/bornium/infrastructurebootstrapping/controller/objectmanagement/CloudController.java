package com.bornium.infrastructurebootstrapping.controller.objectmanagement;

import com.bornium.infrastructurebootstrapping.entities.cloud.Cloud;
import com.bornium.infrastructurebootstrapping.repositories.CloudRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/clouds")
public class CloudController extends ObjectController<Cloud, CloudRepository> {
}
