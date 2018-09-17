package com.bornium.boostrappingascode.controller.objectmanagement

import com.bornium.boostrappingascode.entities.cloud.Cloud
import com.bornium.boostrappingascode.repository.CloudRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cloud")
class CloudController extends ObjectController<Cloud, CloudRepository> {
}
