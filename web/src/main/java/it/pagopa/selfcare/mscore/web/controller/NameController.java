package it.pagopa.selfcare.mscore.web.controller;

import io.swagger.annotations.Api;
import it.pagopa.selfcare.mscore.core.NameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
@Api(tags = "product")
public class NameController {//TODO change Name

    private final NameService nameService;//TODO change Name


    @Autowired
    public NameController(NameService nameService) {
        this.nameService = nameService;
    }

}
