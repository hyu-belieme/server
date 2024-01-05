package com.belieme.apiserver.web.controller;

import com.belieme.apiserver.web.responsebody.HealthResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/")
public class RootApiController extends BaseApiController {

  public RootApiController() {
  }

  @GetMapping("")
  public ResponseEntity<HealthResponse> healthCheck() {
    return ResponseEntity.ok(new HealthResponse());
  }
}
