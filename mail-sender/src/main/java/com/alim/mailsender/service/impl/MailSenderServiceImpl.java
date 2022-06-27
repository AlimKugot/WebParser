package com.alim.mailsender.service.impl;

import com.alim.mailsender.exception.BusinessException;
import com.alim.mailsender.service.MailSenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;


@Service
@RequiredArgsConstructor
public class MailSenderServiceImpl implements MailSenderService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;


    @Override
    @Async
    public void sendMessage(String recipient, String link) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            helper.setFrom(fromEmail);
            helper.setTo(recipient);
            helper.setSubject("Confirm Email");
            helper.setText(buildEmail(link), true);

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new BusinessException("failed to send email: " + e.getMessage());
        }
    }

    private String buildEmail(String link) {
        return """
                <div style="font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c">
                        <span style="display:none;font-size:1px;color:#fff;max-height:0"></span>
                          <table role="presentation" style="border-collapse:collapse;min-width:100%;width:100%!important" width="100%" cellspacing="0" cellpadding="0" border="0">
                            <tbody><tr>
                              <td width="100%" height="53" bgcolor="#0b0c0c">
                                <table role="presentation" style="border-collapse:collapse;max-width:580px" width="100%" cellspacing="0" cellpadding="0" border="0" align="center">
                                  <tbody><tr>
                                    <td width="70" valign="middle" bgcolor="#0b0c0c">
                                        <table role="presentation" style="border-collapse:collapse" cellspacing="0" cellpadding="0" border="0">
                                          <tbody><tr>
                                            <td style="padding-left:10px">
                                            </td>
                                            <td style="font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px">
                                              <span style="font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block">                           Подтверждение почты                       </span>
                                            </td>
                                          </tr>
                                        </tbody></table>
                                    </td>
                                  </tr></tbody>
                                </table>
                              </td>
                            </tr>
                          </tbody></table>
                          <table role="presentation" class="m_-6186904992287805515content" style="border-collapse:collapse;max-width:580px;width:100%!important" width="100%" cellspacing="0" cellpadding="0" border="0" align="center">
                            <tbody><tr>
                              <td width="10" valign="middle" height="10"></td>
                              <td>
                                    <table role="presentation" style="border-collapse:collapse" width="100%" cellspacing="0" cellpadding="0" border="0">
                                      <tbody><tr>
                                        <td width="100%" height="10" bgcolor="#1D70B8"></td>
                                      </tr>
                                    </tbody></table>
                              </td>
                              <td width="10" valign="middle" height="10"></td>
                            </tr>
                          </tbody></table>
                          <table role="presentation" class="m_-6186904992287805515content" style="border-collapse:collapse;max-width:580px;width:100%!important" width="100%" cellspacing="0" cellpadding="0" border="0" align="center">
                            <tbody>
                                <tr>
                                  <td height="30"><br></td>
                                </tr>
                                <tr>
                                  <td width="10" valign="middle"><br></td>
                                  <td style="font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px">
                                        <p style="Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c">
                                               С Вашей почты был запрос на регистрацию
                                       </p>
                                       <p style="Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c">
                                           Чтобы подтвердить свою почту, пожалуйста, перейдите по ссылке ниже:
                                       </p>
                                       <blockquote style="Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px">
                                             <p style="Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c">
                """
                + "<a href=\"" + link + "\">" + link + "</a>" +
                """
                                                     </p>
                                               </blockquote>
                                               Ссылка активна в течение 15 минут.
                                          </td>
                                          <td width="10" valign="middle"><br></td>
                                        </tr>
                                        <tr>
                                          <td height="30"><br></td>
                                        </tr>
                                  </tbody></table><div class="yj6qo"></div><div class="adL">
                                </div>
                        """;
    }
}
