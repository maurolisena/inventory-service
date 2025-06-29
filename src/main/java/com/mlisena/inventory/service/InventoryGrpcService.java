package com.mlisena.inventory.service;

import com.mlisena.inventory.application.InventoryManager;
import com.mlisena.inventory.model.Inventory;
import com.mlisena.inventory.mapper.InventoryGrpcMapper;
import inventory.Inventory.GetInventoriesRequest;
import inventory.Inventory.GetInventoryRequest;
import inventory.Inventory.InventoriesResponse;
import inventory.Inventory.InventoryResponse;
import inventory.InventoryClientGRPCGrpc.InventoryClientGRPCImplBase;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.List;
import java.util.Map;

@GrpcService
@RequiredArgsConstructor
@Slf4j
public class InventoryGrpcService extends InventoryClientGRPCImplBase {

    private final InventoryManager inventoryManager;

    @Override
    public void getInventory(
            GetInventoryRequest request,
            StreamObserver<InventoryResponse> responseObserver
    ) {
        log.info("Getting inventory for product with SKU code: {}", request.getSkuCode());
        Inventory inventory = inventoryManager.getProductInventory(request.getSkuCode());
        log.info("Inventory found for SKU code: {} with quantity: {}", inventory.getSkuCode(), inventory.getQuantity());
        responseObserver.onNext(InventoryGrpcMapper.toInventoryResponse(inventory));
        responseObserver.onCompleted();
    }

    @Override
    public void getInventories(
            GetInventoriesRequest request,
            StreamObserver<InventoriesResponse> responseObserver
    ) {
        log.info("Getting inventory for product to SKU code list");
        List<Inventory> inventories = inventoryManager.getProductInventories(request.getSkuCodesList());

        if (inventories.isEmpty()) {
            log.warn("No inventory records found for give SKU code list");
            responseObserver.onNext(InventoriesResponse.newBuilder().build());
            responseObserver.onCompleted();
            return;
        }

        Map<String, Inventory> inventoryMap = inventoryManager
                .getInventoriesInBatch(request.getSkuCodesList(), inventories);
        log.info("Found {} inventory records for SKU code list", inventoryMap.size());

        InventoriesResponse response = InventoriesResponse.newBuilder().addAllInventories(
            inventoryMap
                .values()
                .stream()
                .map(InventoryGrpcMapper::toInventoryResponse)
                .toList())
            .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
