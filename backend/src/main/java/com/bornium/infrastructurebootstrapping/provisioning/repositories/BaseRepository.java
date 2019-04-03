//package com.bornium.infrastructurebootstrapping.provisioning.repositories;
//
//import com.bornium.infrastructurebootstrapping.provisioning.entities.BaseId;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.repository.NoRepositoryBean;
//
//import java.util.Optional;
//
//@NoRepositoryBean
//public interface BaseRepository<T> extends JpaRepository<T, Long> {
//    public abstract Optional<T> findOneByBaseId(BaseId baseId);
//
//    public abstract Optional<T> deleteByBaseId(BaseId baseId);
//}
