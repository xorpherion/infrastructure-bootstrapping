package com.bornium.infrastructurebootstrapping.base.controller.objectmanagement;

import com.bornium.infrastructurebootstrapping.provisioning.entities.Namespace;
import com.bornium.infrastructurebootstrapping.provisioning.repositories.NamespaceRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/namespaces")
public class NamespaceController extends ObjectController<Namespace, NamespaceRepository> {
}
