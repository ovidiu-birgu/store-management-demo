package org.example.storeapi.repository;

import org.example.storeapi.entity.StoreOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreOrderRepository extends JpaRepository<StoreOrder, Long> {
    Page<StoreOrder> findByCustomerUsername(String customer, Pageable pageable);
}
