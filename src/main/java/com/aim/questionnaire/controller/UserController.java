package com.aim.questionnaire.controller;

import com.aim.questionnaire.beans.HttpResponseEntity;
import com.aim.questionnaire.common.Constans;
import com.aim.questionnaire.common.utils.Base64Helper;
import com.aim.questionnaire.common.utils.CompareText;
import com.aim.questionnaire.common.utils.ExcelUtil;
import com.aim.questionnaire.common.utils.FaceMatch;
import com.aim.questionnaire.dao.entity.ProjectEntity;
import com.aim.questionnaire.dao.entity.UserEntity;
import com.aim.questionnaire.service.ProjectService;
import com.aim.questionnaire.service.UserService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import schemasMicrosoftComVml.CTH;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.*;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
//@RequestMapping("/admin")
public class UserController {
    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;


    @RequestMapping(value = "/admin/userLogin", method = RequestMethod.POST, headers = "Accept=application/json")
    @ResponseBody
    public HttpResponseEntity userLogin(@RequestBody UserEntity userEntity) {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();

        UserEntity userHas = userService.selectAllByName(userEntity.getUsername());
//        System.out.println(userHas);
        if (userHas == null) {
            httpResponseEntity.setData(null);
            httpResponseEntity.setCode(Constans.SUCCESS_CODE);
            httpResponseEntity.setMessage("用户不存在!");
            return httpResponseEntity;
        }
        if (userEntity.getPassword().equals(userHas.getPassword())) {
            httpResponseEntity.setData(userHas);
            httpResponseEntity.setCode(Constans.SUCCESS_CODE);
            httpResponseEntity.setMessage("登录成功!");
        } else {
            httpResponseEntity.setData(userHas);
            httpResponseEntity.setCode(Constans.SUCCESS_CODE);
            httpResponseEntity.setMessage("密码错误!");
        }
//        System.out.println(httpResponseEntity);
        return httpResponseEntity;

    }


    @RequestMapping(value = "/admin/FaceLogin", method = RequestMethod.POST, headers = "Accept=application/json")
    @ResponseBody
    public HttpResponseEntity userLoginByFace(@RequestBody Map<String, String> map) {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        String imagePath = map.get("imgpath");
        String localImgPath = "C:/Users/jiongjiong/Desktop/002.jpg";
        List<Map<String, String>> list = new ArrayList<>();
        Map<String, String> params = new HashMap<>();
        params.put("image", imagePath);
        params.put("image_type", "BASE64");
        params.put("face_type", "LIVE");
        params.put("quality_control", "LOW");
        params.put("liveness_control", "LOW");
        list.add(params);
        Map<String, String> params_2 = new HashMap<>();
        params_2.put("image", Base64Helper.ImageToBase64ByLocal(localImgPath));
        params_2.put("image_type", "BASE64");
        params_2.put("face_type", "LIVE");
        params_2.put("quality_control", "LOW");
        params_2.put("liveness_control", "LOW");
        list.add(params_2);
        String result = FaceMatch.faceMatch(list);
        String reg = "\\{\"score\":(\\d+.\\d+),";
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(result);
        Double score = 0.0;
        if (m.find()) {
            score = Double.parseDouble(m.group(1));
        }
        System.out.println(score);
        if (score >= 90) {
            UserEntity userHas = userService.selectAllByName("admin");
            httpResponseEntity.setData(userHas);
            httpResponseEntity.setCode(Constans.SUCCESS_CODE);
            httpResponseEntity.setMessage("登陆成功!");
        } else {
            httpResponseEntity.setData(null);
            httpResponseEntity.setCode(Constans.LOGIN_USERNAME_MESSAGE);
            httpResponseEntity.setMessage("登陆失败!");
        }
        return httpResponseEntity;
    }


    @RequestMapping(value = "/admin/addUserInfo", method = RequestMethod.POST, headers = "Accept=application/json")
    @ResponseBody
    public HttpResponseEntity userRegister(@RequestBody UserEntity userEntity) {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
//        System.out.println("hahha"+userEntity.getUsername()+" "+userEntity.getPassword());
        UserEntity useHas = userService.selectAllByName(userEntity.getUsername());
        System.out.println(useHas);
        if (useHas != null) {
            httpResponseEntity.setData(null);
            httpResponseEntity.setCode(Constans.USER_USERNAME_CODE);
            httpResponseEntity.setMessage("用户名已存在!");
            return httpResponseEntity;
        } else {
            userEntity.setStatus("1");
            userEntity.setCreatedBy("admin");
            userEntity.setLastUpdatedBy("admin");
            userEntity.setLastUpdateDate(userEntity.getCreationDate());
            int result = userService.insert(userEntity);

            httpResponseEntity.setData(result);
            httpResponseEntity.setCode(Constans.SUCCESS_CODE);
            httpResponseEntity.setMessage(Constans.ADD_MESSAGE);
            return httpResponseEntity;
        }


    }

    @RequestMapping(value = "/admin/queryUserList", method = RequestMethod.POST, headers = "Accept=application/json")
    @ResponseBody
    public HttpResponseEntity showUserList(@RequestBody Map<String, String> map) {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        int pageNums = Integer.parseInt(map.get("pageNum"));
        int pageSize = Integer.parseInt(map.get("pageSize"));
        int start = (pageNums - 1) * 10;
        int end = pageSize;
        int sum = Integer.parseInt(userService.getCount());
        Map<String, Integer> queryMap = new HashMap<>();
        queryMap.put("start", start);
        queryMap.put("end", end);
        String userName = map.get("userName");

        List<UserEntity> result = userService.queryUserList(queryMap);
        List<UserEntity> indistinct_result = new ArrayList<>();
        Map<String, Object> resultMap = new HashMap<>();

        System.out.println(userName == "");
        System.out.println(pageNums + " " + pageSize + " " + userName + " " + sum);
        if (userName != "") {
            Map<String, Object> map1 = new HashMap<>();
            map1.put("username", userName);
            map1.put("start", start);
            map1.put("end", end);
            indistinct_result = userService.queryUserListIndistinct(map1);
            resultMap.put("data", indistinct_result);
            resultMap.put("total", indistinct_result.size());
        } else {
            resultMap.put("data", result);
            resultMap.put("total", sum);
        }
        httpResponseEntity.setData(resultMap);
        httpResponseEntity.setCode(Constans.SUCCESS_CODE);
        httpResponseEntity.setMessage("查询成功!");
        return httpResponseEntity;
    }


    @RequestMapping(value = "/admin/updatePassword", method = RequestMethod.POST, headers = "Accept=application/json")
    @ResponseBody
    public HttpResponseEntity resetPassword(@RequestBody UserEntity userEntity) {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();

        int result = userService.resetPassword(userEntity.getUsername());
        System.out.println(result);
        httpResponseEntity.setData(result);
        httpResponseEntity.setCode(Constans.SUCCESS_CODE);
        httpResponseEntity.setMessage(Constans.ADD_MESSAGE);
        return httpResponseEntity;
    }


    @RequestMapping(value = "/admin/AiService", method = RequestMethod.POST, headers = "Accept=application/json")
    @ResponseBody
    public HttpResponseEntity AiServiceAnswer(@RequestBody Map<String, String> map) {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        String question = map.get("dialog");
        Map<String, String> params = new HashMap<>();
        params.put("text_1", question);
        params.put("text_2", null);
        System.out.println(question);
        List<Map<String, String>> maps = userService.getAiAnswer();
        Map<String, Object> result = new HashMap<>();
        for (int i = 0; i < maps.size(); i++) {
            params.put("text_2", maps.get(i).get("question"));
            result = CompareText.compareText(params);
            System.out.println(result);
            if (result.get("score") == null) {
                Map<String, String> data = new HashMap<>();
                data.put("answer", "对不起，我不太明白您的问题呢？");
                httpResponseEntity.setData(data);
                httpResponseEntity.setCode("666");
                httpResponseEntity.setMessage("获得回答成功!");
                return httpResponseEntity;

            }
            if (Double.parseDouble(result.get("score").toString()) >= 0.80) {
                Map<String, String> data = new HashMap<>();
                data.put("answer", maps.get(i).get("answer"));
                httpResponseEntity.setData(data);
                break;
            }

        }
        httpResponseEntity.setCode("666");
        httpResponseEntity.setMessage("获得回答成功!");
        return httpResponseEntity;
    }


//    @RequestMapping(value = "/admin/selectUserListToExcel", method = RequestMethod.GET)
//    public void outputExcel(HttpServletRequest request, HttpServletResponse response) {
//        String userName=request.getParameter("userName");
//        String[] titles = {"用户id", "账号", "密码", "开始时间", "结束时间", "是否启用"};
//
//        String a;
//
//        List<UserEntity> result = userService.queryUserList();
//        List<UserEntity> indistinct_result = new ArrayList<>();
//        List<String> list = new ArrayList<>();
//        List<List<String>> lists = new ArrayList<>();
//        if (userName != null) {
//            for (int i = 0; i < result.size(); i++) {
//                String username = result.get(i).getUsername();
//                if (username.indexOf(userName) != -1) {
//                    list.add(result.get(i).getId());
//                    list.add(result.get(i).getUsername());
//                    list.add(result.get(i).getPassword());
//                    list.add(result.get(i).getStartTime().toString());
//                    list.add(result.get(i).getStopTime().toString());
//                    list.add(result.get(i).getStatus());
//                }
//            }
//            lists.add(list);
//        } else {
//            for (int i = 0; i < result.size(); i++) {
//                list.add(result.get(i).getId());
//                list.add(result.get(i).getUsername());
//                list.add(result.get(i).getPassword());
//                list.add(result.get(i).getStartTime().toString());
//                list.add(result.get(i).getStopTime().toString());
//                list.add(result.get(i).getStatus());
//            }
//            lists.add(list);
//        }
//
//        HSSFWorkbook wb = new HSSFWorkbook();
//        wb=ExcelUtil.getHSSFWorkbook("用户信息表", titles, lists, wb);
//        Row row=wb.getSheetAt(0).getRow(0);
//        for (Cell cell : row) {
//            System.out.println("asdasd"+cell.getStringCellValue());
//        }
//
//        ServletContext application = request.getServletContext();
//        String path = application.getRealPath("excel");
//        path = "C:/Users/jiongjiong/Downloads";
//        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        String date=fmt.format(LocalDateTime.now());
//        String fileName = date+"example"+".xls";//最终命名
//        path += File.separator +fileName;//最终路径
//        System.out.println(path);
//        download(request,response, fileName,path);//具体导出的方法
//    }

    public static Boolean download(HttpServletRequest request, HttpServletResponse response, String fileName, String path) {
        // 1、构建流的对象
        InputStream is = null;
        OutputStream out = null;
        // 2、设置响应头
        try {
            response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 3、设置响应的数据类型
        response.setContentType("multipart/form-data");
        // 4、边读边写
        byte[] bt = new byte[1024];
        int len;
        try {
            is = new FileInputStream(path);
            out = response.getOutputStream();
            while ((len = is.read(bt)) != -1) {
                out.write(bt, 0, len);
            }
            out.flush();
            System.out.println("表下载成功!");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 5、关闭资源
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            // 6、删除源文件
            new File(path).delete();
        }
        return false;
    }


}
