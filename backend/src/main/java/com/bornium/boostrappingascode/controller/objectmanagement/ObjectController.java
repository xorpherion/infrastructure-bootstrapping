package com.bornium.boostrappingascode.controller.objectmanagement;

import com.bornium.boostrappingascode.entities.Base;
import com.bornium.boostrappingascode.entities.BaseId;
import com.bornium.boostrappingascode.repositories.BaseRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequestMapping("/objects/{nid}")
public abstract class ObjectController<T extends Base, S extends BaseRepository<T>> {
    @GetMapping
    public List<T> getAll() {
        return repo.findAll();
    }

    @GetMapping("/{oid}")
    public Optional<T> getOne(@PathVariable("nid") String nid, @PathVariable("oid") String oid) {
        return repo.findOneByBaseId(new BaseId(oid, nid));
    }

    @PostMapping
    public T create(@RequestBody T obj) {
        return repo.save(obj);
    }

    @PutMapping("/{oid}")
    public Optional<T> modify(@PathVariable("nid") String nid, @PathVariable("oid") String oid, @RequestBody T obj) throws Exception {
        Optional<T> found = repo.findOneByBaseId(new BaseId(oid, nid));
        if (found.isPresent()) return Optional.empty();
        Map original = toMap(found.get());
        Map update = toMap(obj);
        original.putAll(update);
        T modified = (T) toObj(original, (Class<?>) obj.getClass());
        return Optional.of(repo.save(modified));
    }

    @DeleteMapping("/{oid}")
    public Optional<T> delete(@PathVariable("nid") String nid, @PathVariable("oid") String oid) {
        return repo.deleteByBaseId(new BaseId(nid, oid));
    }

    public <I> Map toMap(I obj) throws IOException {
        return mapper.readValue(mapper.writeValueAsString(obj), Map.class);
    }

    public <I> I toObj(Map map, Class<I> clazz) throws IOException {
        return mapper.readValue(mapper.writeValueAsString(map), clazz);
    }

    public S getRepo() {
        return repo;
    }

    public void setRepo(S repo) {
        this.repo = repo;
    }

    public ObjectMapper getMapper() {
        return mapper;
    }

    public void setMapper(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Autowired
    private S repo;
    @Autowired
    private ObjectMapper mapper;
}
