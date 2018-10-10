package com.bornium.boostrappingascode.controller.objectmanagement;

import com.bornium.boostrappingascode.entities.cloud.Cloud;
import com.bornium.boostrappingascode.repositories.CloudRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/clouds")
public class CloudController extends ObjectController<Cloud, CloudRepository> {
}
