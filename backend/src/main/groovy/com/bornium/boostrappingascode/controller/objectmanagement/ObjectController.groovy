package com.bornium.boostrappingascode.controller.objectmanagement

import com.bornium.boostrappingascode.entities.Base
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

abstract class ObjectController<T extends Base,S extends JpaRepository<T,Long>> {

    @Autowired
    S repo;

    @GetMapping("/")
    public <T> T getAll(){
        return repo.findAll();
    }

    @GetMapping("/{oid}")
    public <T> T getOne(@RequestParam("oid") String oid){
        return repo.findOne(oid);
    }
}
