package com.bornium.boostrappingascode.controller.objectmanagement

import com.bornium.boostrappingascode.entities.Base
import com.bornium.boostrappingascode.entities.BaseId
import com.bornium.boostrappingascode.repository.BaseRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RequestMapping("/objects/{nid}")
abstract class ObjectController<T extends Base, S extends BaseRepository<T>> {

    @Autowired
    S repo

    @Autowired
    ObjectMapper mapper

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
        if (found.isPresent())
            return Optional.empty()
        Map original = toMap(found.get())
        Map update = toMap(obj)
        original.putAll(update)
        T modified = toObj(original, obj.getClass())
        return Optional.of(repo.save(modified))
    }

    @DeleteMapping("/{oid}")
    Optional<T> delete(@PathVariable("nid") String nid, @PathVariable("oid") String oid) {
        return repo.deleteByBaseId(new BaseId(nid, oid))
    }

    public <I> Map toMap(I obj) {
        return mapper.readValue(mapper.writeValueAsString(obj), Map.class)
    }

    public <I> I toObj(Map map, Class<I> clazz) {
        return mapper.readValue(mapper.writeValueAsString(map), clazz)
    }

}
