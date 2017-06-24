package com.Music.web;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
@RequestMapping(value = "/")
public class TestController {
	private final Logger logger=Logger.getLogger(TestController.class);
  @RequestMapping(value = "index")
  public String test() {
	  logger.info("没别的意思");
	  logger.error("就是为了测试");
	  logger.debug("也是为了测试");
	  return "demoindex";
  }
}
