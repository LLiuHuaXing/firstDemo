package com.sxt.sys.util;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

public class OperationCodeUtil extends HttpServlet {
    private static final int WIDTH = 100;//设置验证码图片宽度
    private static final int HEIGHT = 40;//设置验证码图片高度
    private static final int LENGTH = 4;//设置验证码长度
    private static final int NUMBER=9;  //设置数字的范围

    //设置验证码随机出现的运算符
    private static final String str = "*/+-";
    static char[] chars = str.toCharArray();//将字符放在数组中方便随机读取
    private static Random random = new Random();


    //生成验证码
    public static BufferedImage getCheckCode(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //设置输出的类型为图片
        response.setContentType("image/jpeg");
        //设置不进行缓存
        response.setHeader("pragma", "no-cache");
        response.setHeader("cache-control", "no-cache");
        response.setHeader("expires", "0");

        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_3BYTE_BGR);

        //画笔
        Graphics graphics = image.getGraphics();

        //设置背景颜色并绘制矩形背景
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, WIDTH, HEIGHT);

        //用于记录生成的运算符的结果
        int ResultCode =0;

        /*生成运算符验证码并绘制*/
        int x=random.nextInt(NUMBER);
        int y=random.nextInt(NUMBER);
        String c=""+chars[random.nextInt(str.length())];
//        System.out.println("str "+str.length());
        //计算结果
        if (c.equals("*")){
            ResultCode=x*y;
        }else if (c.equals("/")){
            //防止by zero
            if(x==0 || y==0){
                ResultCode=0;
            } else {
                ResultCode=x>y?x/y:y/x;
            }
        }else if (c.equals("+")){
            ResultCode=x+y;
        }else {
            ResultCode=x>y?x-y:y-x;
        }

        String newStr=x+c+y+"=?";
        String newResultCode=String.valueOf(ResultCode);
        //绘制
        graphics.setColor(getColor());
        // 设置字体
        graphics.setFont(new Font("隶书", Font.BOLD, 25));
        // 表示这段文字在图片上的位置(x,y) .第一个是你设置的内容。
        graphics.drawString(newStr, 10, 28);


        System.out.println("验证码  "+newResultCode);
        //生成干扰点
        for (int i = 0; i < 100; i++) {
            graphics.setColor(getColor());
            graphics.drawOval(random.nextInt(100), random.nextInt(50), 1, 1);
        }

        //将生成的验证码存入session中，以便进行校验
        HttpSession session = request.getSession();
        session.setAttribute("code", newResultCode);

        //绘制图片
        graphics.dispose();
        return image;

        //将图片输出到response中
//        ImageIO.write(image, "JPEG", response.getOutputStream());

    }

    //随机生成颜色
    private static Color getColor() {
        return new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255));
    }


}
