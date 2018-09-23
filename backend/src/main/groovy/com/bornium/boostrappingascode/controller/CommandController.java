package com.bornium.boostrappingascode.controller;

import com.bornium.boostrappingascode.commands.CreateCloud;
import com.bornium.boostrappingascode.entities.cloud.Cloud;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/commands")
public class CommandController {

    final CreateCloud createCloud;

    public CommandController(CreateCloud createCloud) {
        this.createCloud = createCloud;
    }

    @PostMapping("/createcloud")
    public Cloud createCloud(@RequestBody Cloud cloud) {
        return createCloud.createCloud(cloud);
    }


}
