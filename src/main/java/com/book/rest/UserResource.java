package com.book.rest;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.book.bo.Message;
import com.book.bo.Status;
import com.book.common.Role;
import com.book.entity.Book;
import com.book.entity.User;
import com.book.service.UserService;
import com.book.vo.PageResultVo;
import com.book.vo.UserListVo;
import com.book.vo.UserVo;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;


/**
 * Created by admin on 2016/4/25.
 * 用户资源类
 */
@Controller
@RequestMapping("/user")
public class UserResource {

    /**日志工具*/
    private static final Logger logger = Logger.getLogger(UserResource.class);

    @Autowired
    private UserService userService;

    /**
     * 登录
     * @param userVo 登录的用户名密码
     * @return
     */
    @RequestMapping(value = "/login",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Message login(@RequestBody UserVo userVo){
        Message msg = new Message();
        try {
            if(userVo==null || StringUtils.isBlank(userVo.getUser_name()) || StringUtils.isBlank(userVo.getPassword())){
                msg.setStatus(Status.ERROR);
                msg.setStatusMsg("error");

                return msg;
            }

            Role role = userService.login(userVo);
            if(role == null){
                msg.setStatus(Status.ERROR);
                msg.setStatusMsg("用户名密码错误！");
            }else{
                msg.setStatus(Status.SUCCESS);
                JSONObject json = new JSONObject();
                json.put("role", role);
                msg.setData(json.toString());
            }

        }catch (Exception e){
            logger.error(e.getMessage(), e);
            msg.setStatus(Status.ERROR);
            msg.setStatusMsg(e.getMessage());
        }
        return msg;
    }

    /**
     * {"valid":true}
     * @param username
     * @return
     */
    @RequestMapping(value = "/valid",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody JSONObject validUserName( String username){
        JSONObject json = null;

        try{
            boolean result = userService.validUserName(username);
            json = new JSONObject();
            json.put("valid", result);
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            json = new JSONObject();
            json.put("valid",false);
        }

        return json;
    }

    /**
     *  用户注册
     * @param userVo 用户注册信息
     * @return 用户id
     */
    @RequestMapping(value = "/register",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Message register(@RequestBody UserVo userVo){
        Message msg  = new Message();
        try{
            // 校验数据
            if(userVo == null || StringUtils.isBlank(userVo.getUser_name())
                    ||StringUtils.isBlank(userVo.getPassword()) || userVo.getRole()==null){
                msg.setStatus(Status.ERROR);
                msg.setStatusMsg("数据错误");
                return msg;
            }

            String userId = userService.register(userVo);
            msg.setStatus(Status.SUCCESS);
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            msg.setStatus(Status.ERROR);
            msg.setStatusMsg(e.getMessage());
        }
        return msg;
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    PageResultVo list(@RequestParam("pageSize") int pageSize, @RequestParam("pageNum") int pageNum,
                      @RequestParam(value = "userName", required = false) String userName){
        PageResultVo vo = new PageResultVo(true);

        List<UserListVo> list =  userService.list(pageSize, pageNum, userName);
        int total = userService.list(userName);
        int count  = total%pageSize==0 ? total/pageSize : total/pageSize+1;
        vo.setCount(count);
        vo.setPageNum(pageNum);
        vo.setData(JSONArray.toJSONString(list, SerializerFeature.DisableCircularReferenceDetect));
        vo.setFlag(true);

        return  vo;
    }

    @RequestMapping(value = "/user_name",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Message getUserName(){
        Message msg  = new Message();
        try{
            User user = userService.getUserName();
            msg.setStatus(Status.SUCCESS);
            msg.setData(JSONObject.toJSONString(user));

        }catch (Exception e){
            logger.error(e.getMessage(), e);
            msg.setStatus(Status.ERROR);
            msg.setStatusMsg(e.getMessage());
        }
        return msg;
    }
    @RequestMapping(value = "/getPermission",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Message getPermission(){
        Message msg = new Message();
        try {
            msg.setData(JSONArray.toJSONString(userService.getUserPermission(), SerializerFeature.DisableCircularReferenceDetect));
            msg.setStatus(Status.SUCCESS);
        }catch (Exception e){
            logger.error(e, e);
            msg.setStatus(Status.ERROR);
            msg.setError(e.getMessage());
        }

        return msg;
    }
    @RequestMapping(value = "/reset_password",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Message resetPassword(@RequestParam("userId") String userId){
        Message msg = new Message();
        try {
            if(StringUtils.isBlank(userId)){
                msg.setStatus(Status.ERROR);
                msg.setStatusMsg("userId is empty");

                return msg;
            }

            userService.resetPassword(userId);
            msg.setStatus(Status.SUCCESS);


        }catch (Exception e){
            logger.error(e.getMessage(), e);
            msg.setStatus(Status.ERROR);
            msg.setStatusMsg(e.getMessage());
        }
        return msg;
    }
    @RequestMapping(value = "/delete",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Message delete(@RequestParam("userId") String userId){
        Message msg = new Message();
        try {
            if(StringUtils.isBlank(userId)){
                msg.setStatus(Status.ERROR);
                msg.setStatusMsg("userId is empty");

                return msg;
            }

            userService.delete(userId);
            msg.setStatus(Status.SUCCESS);


        }catch (Exception e){
            logger.error(e.getMessage(), e);
            msg.setStatus(Status.ERROR);
            msg.setStatusMsg(e.getMessage());
        }
        return msg;
    }
    @RequestMapping(value = "/change_password",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Message changePassword(@RequestParam("password") String password){
        Message msg = new Message();
        try {
            if(password == null || StringUtils.isBlank(password.trim())){
                msg.setStatus(Status.ERROR);
                msg.setStatusMsg("password is empty");
            }
            userService.changePassword(password);
            msg.setStatus(Status.SUCCESS);


        }catch (Exception e){
            logger.error(e.getMessage(), e);
            msg.setStatus(Status.ERROR);
            msg.setStatusMsg(e.getMessage());
        }
        return msg;
    }






}
