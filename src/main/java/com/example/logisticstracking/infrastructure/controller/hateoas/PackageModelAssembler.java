package com.example.logisticstracking.infrastructure.controller.hateoas;

import com.example.logisticstracking.application.dto.PackageDetailsDTO;
import com.example.logisticstracking.infrastructure.controller.PackageController;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class PackageModelAssembler implements RepresentationModelAssembler<PackageDetailsDTO, PackageDetailsDTO> {

    @Override
    @NonNull
    public PackageDetailsDTO toModel(@NonNull PackageDetailsDTO packageDetails) {
        if (packageDetails.getLinks().isEmpty()) {
            packageDetails.add(linkTo(methodOn(PackageController.class).getPackageDetails(packageDetails.getId(), false)).withSelfRel());
            packageDetails.add(linkTo(methodOn(PackageController.class).updateStatus(packageDetails.getId(), null)).withRel("update-status"));
            packageDetails.add(linkTo(methodOn(PackageController.class).cancelPackage(packageDetails.getId())).withRel("cancel"));
        }

        return packageDetails;
    }
}
