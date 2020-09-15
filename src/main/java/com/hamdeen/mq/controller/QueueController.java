package com.hamdeen.mq.controller;

import com.hamdeen.mq.dto.QueueConfiguration;
import com.hamdeen.mq.service.QueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "jms")
public class QueueController {

    @Autowired
    private QueueService queueService;

    @PostMapping("/send/{correlationIdSend}")
    public ResponseEntity<?> send(@RequestBody QueueConfiguration config, @PathVariable String correlationIdSend) throws Exception {
        try {
//            queueService.SendMessage(config, correlationIdSend, msg);
            return new ResponseEntity<>("Message sent successfuly", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

//    @GetMapping("/receive/{correlationIdReceive}")
//    public ResponseEntity<?> receive(@RequestBody QueueConfiguration config, @PathVariable String correlationIdReceive) throws Exception {
//        try {
//            return new ResponseEntity<>(queueService.Receive(config, correlationIdReceive), HttpStatus.OK);
//        } catch (Exception e) {
//            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
//        }
//    }
}
