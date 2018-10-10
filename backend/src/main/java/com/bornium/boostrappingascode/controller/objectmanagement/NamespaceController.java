package com.bornium.boostrappingascode.controller.objectmanagement;

import com.bornium.boostrappingascode.entities.Namespace;
import com.bornium.boostrappingascode.repositories.NamespaceRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/namespaces")
public class NamespaceController extends ObjectController<Namespace, NamespaceRepository> {
}
