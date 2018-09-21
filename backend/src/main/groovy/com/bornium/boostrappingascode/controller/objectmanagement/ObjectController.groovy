package com.bornium.boostrappingascode.controller.objectmanagement

import com.bornium.boostrappingascode.entities.Base
import com.bornium.boostrappingascode.entities.BaseId
import com.bornium.boostrappingascode.repository.BaseRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RequestMapping("/objects/{nid}")
abstract class ObjectController<T extends Base, S extends BaseRepository<T>> {

    @Autowired
    S repo;

    @GetMapping
    List<T> getAll() {
        return repo.findAll()
    }

    @GetMapping("/{oid}")
    Optional<T> getOne(@PathVariable("nid") String nid, @PathVariable("oid") String oid) {
        return repo.findOneByBaseId(new BaseId(oid, nid))
    }

    @PostMapping
    T create(@RequestBody T obj) {
        return repo.save(obj)
    }

    @PutMapping("/{oid}")
    Optional<T> modify(@PathVariable("nid") String nid, @PathVariable("oid") String oid, @RequestBody T obj) {
        Optional<T> found = repo.findOneByBaseId(new BaseId(oid, nid))
        //modify
        if (found.isPresent())
            return repo.save(found)
        return Optional.empty()
    }

    @DeleteMapping("/{oid}")
    Optional<T> delete(@PathVariable("nid") String nid, @PathVariable("oid") String oid) {
        return repo.deleteByBaseId(new BaseId(nid, oid))
    }

}
