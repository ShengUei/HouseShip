package com.grp4.houseship.email.service;

import com.grp4.houseship.house.model.HouseInfo;
import com.grp4.houseship.member.model.Member;
import com.grp4.houseship.order.model.Order;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

@Service
public class EmailService {

    private TemplateEngine templateEngine;

    private JavaMailSender javaMailSender;

    public EmailService(TemplateEngine templateEngine, JavaMailSender javaMailSender) {
        this.templateEngine = templateEngine;
        this.javaMailSender = javaMailSender;
    }

    public String sendMail(Member member) throws MessagingException{
        Context context = new Context();
        context.setVariable("member", member);

        String process = templateEngine.process("emailExample", context);
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,true);

        helper.setSubject("Hello");
        helper.setText(process,true);
//        helper.setTo(member.getEmail());
        helper.setTo("p26074101@gs.ncku.edu.tw");

        //靜態資源
        FileSystemResource file = new FileSystemResource( "src/main/resources/static/images/logo.png" );
        helper.addInline("image", file);

        javaMailSender.send(mimeMessage);
        return "Sent";
    }

    public String sendOrderMail(Order order, String template, String title) throws MessagingException{
        Context context = new Context();
        context.setVariable("order", order);

        String process = templateEngine.process(template, context);
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,true);

        helper.setSubject(title);
        helper.setText(process,true);
        helper.setTo(order.getOrderDetail().getGuest().getEmail());

        //靜態資源
        FileSystemResource file = new FileSystemResource( "src/main/resources/static/images/logo.png" );
        helper.addInline("logo", file);

        javaMailSender.send(mimeMessage);
        return "Sent";
    }

    public String sendHouseMail(HouseInfo houseInfo, String template, String title) throws MessagingException{
        Context context = new Context();
        context.setVariable("houseInfo", houseInfo);

        String process = templateEngine.process(template, context);
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,true);

        helper.setSubject(title);
        helper.setText(process,true);
        helper.setTo(houseInfo.getMember().getEmail());

        //靜態資源
        FileSystemResource file = new FileSystemResource( "src/main/resources/static/images/logo.png" );
        helper.addInline("logo", file);

        javaMailSender.send(mimeMessage);
        return "Sent";
    }
}
