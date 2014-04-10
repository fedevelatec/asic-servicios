package com.fedevela.dms.services.impl;

/**
 * Created by fvelazquez on 9/04/14.
 */
import com.fedevela.asic.util.TypeCast;
import com.fedevela.dms.services.MailService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

@Service
public class MailServiceImpl implements MailService {

    @Resource
    private JavaMailSender sender;

    @Override
    public boolean send(
            final String from,
            final String[] to,
            final String[] cc,
            final String[] bcc,
            final String subject,
            final String body) {
        return send(from, to, cc, bcc, subject, body, null, null);
    }

    @Override
    public boolean send(
            final String from,
            final String to,
            final String cc,
            final String bcc,
            final String subject,
            final String body) {
        String[] aTo = TypeCast.isBlank(to) ? null : to.split(",");
        String[] aCc = TypeCast.isBlank(cc) ? null : cc.split(",");
        String[] aBcc = TypeCast.isBlank(bcc) ? null : bcc.split(",");
        return send(from, aTo, aCc, aBcc, subject, body, null, null);
    }

    @Override
    public boolean send(
            final String from,
            final String to,
            final String cc,
            final String bcc,
            final String subject,
            final List<Map<String, Object>> body,
            final Template ftl) {
        MimeMessagePreparator preparator = new MimeMessagePreparator() {
            @Override
            public void prepare(MimeMessage mimeMessage) throws Exception {
                String[] aTo = TypeCast.isBlank(to) ? null : to.split(",");
                String[] aCc = TypeCast.isBlank(cc) ? null : cc.split(",");
                String[] aBcc = TypeCast.isBlank(bcc) ? null : bcc.split(",");
                MimeMessageHelper message = new MimeMessageHelper(mimeMessage, "UTF-8");
                if (aTo != null) {
                    message.setTo(aTo);
                }
                if (aCc != null) {
                    message.setCc(aCc);
                }
                if (aBcc != null) {
                    message.setBcc(aBcc);
                }
                message.setFrom(from);
                message.setSubject(subject);
                Map model = new HashMap();
                model.put("body", body);
                String text = FreeMarkerTemplateUtils.processTemplateIntoString(ftl, model);
                message.setText(text, true);
            }
        };
        sender.send(preparator);
        return true;
    }

    @Override
    public boolean send(
            final String from,
            final String to,
            final String cc,
            final String bcc,
            final String subject,
            final List<Map<String, Object>> body,
            final Template ftl,
            final String attachmentFilename,
            final InputStream attachment) {
        return send(from, to, cc, bcc, subject, body, ftl, new InputStream[]{attachment});
    }

    @Override
    public boolean send(
            final String from,
            final String[] to,
            final String[] cc,
            final String[] bcc,
            final String subject,
            final String body,
            final Configuration cfg,
            final String template) {
        if (TypeCast.isBlank(from)) {
            throw new RuntimeException("Error en el 'from' del correo.");
        }
        if ((to == null) || (to.length < 1)) {
            throw new RuntimeException("Error en el 'to' del correo.");
        }
        MimeMessagePreparator preparator = new MimeMessagePreparator() {
            @Override
            public void prepare(MimeMessage mimeMessage) throws Exception {

                MimeMessageHelper message = new MimeMessageHelper(mimeMessage, "UTF-8");
                message.setFrom(from);
                message.setTo(to);
                if (cc != null) {
                    message.setCc(cc);
                }
                if (bcc != null) {
                    message.setBcc(bcc);
                }
                message.setSubject(subject);
                if (template != null) {
                    Map model = new HashMap();
                    model.put("message", body);
                    message.setText(FreeMarkerTemplateUtils.processTemplateIntoString(cfg.getTemplate(template), model), true);
                } else {
                    message.setText(body, true);
                }
            }
        };
        sender.send(preparator);
        return true;
    }

    @Override
    public boolean send(
            final String from,
            final String to,
            final String cc,
            final String bcc,
            final String subject,
            final Map<String, Object> body,
            final Template ftl,
            final File attachment) {
        return send(from, to, cc, bcc, subject, body, ftl, new File[]{attachment});
    }

    public boolean send(
            final String from,
            final String to,
            final String cc,
            final String bcc,
            final String subject,
            final Map<String, Object> body,
            final Template ftl,
            final File[] attachments) {
        MimeMessagePreparator preparator = new MimeMessagePreparator() {
            @Override
            public void prepare(MimeMessage mimeMessage) throws Exception {
                String[] aTo = TypeCast.isBlank(to) ? null : to.split(",");
                String[] aCc = TypeCast.isBlank(cc) ? null : cc.split(",");
                String[] aBcc = TypeCast.isBlank(bcc) ? null : bcc.split(",");
                MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true);

                if (aTo != null) {
                    message.setTo(aTo);
                }
                if (aCc != null) {
                    message.setCc(aCc);
                }
                if (aBcc != null) {
                    message.setBcc(aBcc);
                }

                message.setFrom(from);
                message.setSubject(subject);
                String text = FreeMarkerTemplateUtils.processTemplateIntoString(ftl, body);
                message.setText(text, true);
                for (File file : attachments) {
                    message.addAttachment(file.getName(), new FileSystemResource(file));
                }

            }
        };
        sender.send(preparator);
        return true;
    }

    @Override
    public boolean send(
            final String from,
            final String to,
            final String cc,
            final String bcc,
            final String subject,
            final List<Map<String, Object>> body,
            final Template ftl,
            final File attachment) {
        return send(from, to, cc, bcc, subject, body, ftl, new File[]{attachment});
    }

    @Override
    public boolean send(
            final String from,
            final String to,
            final String cc,
            final String bcc,
            final String subject,
            final List<Map<String, Object>> body,
            final Template ftl,
            final File[] attachments) {
        MimeMessagePreparator preparator = new MimeMessagePreparator() {
            @Override
            public void prepare(MimeMessage mimeMessage) throws Exception {
                String[] aTo = TypeCast.isBlank(to) ? null : to.split(",");
                String[] aCc = TypeCast.isBlank(cc) ? null : cc.split(",");
                String[] aBcc = TypeCast.isBlank(bcc) ? null : bcc.split(",");
                MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true);

                if (aTo != null) {
                    message.setTo(aTo);
                }
                if (aCc != null) {
                    message.setCc(aCc);
                }
                if (aBcc != null) {
                    message.setBcc(aBcc);
                }

                message.setFrom(from);
                message.setSubject(subject);
                Map model = new HashMap();
                model.put("body", body);
                String text = FreeMarkerTemplateUtils.processTemplateIntoString(ftl, model);
                message.setText(text, true);
                for (File file : attachments) {
                    message.addAttachment(file.getName(), new FileSystemResource(file));
                }

            }
        };
        sender.send(preparator);
        return true;
    }

    @Override
    public boolean send(
            final String from,
            final String to,
            final String cc,
            final String bcc,
            final String subject,
            final List<Map<String, Object>> body,
            final Template ftl,
            final InputStream[] attachments) {
        MimeMessagePreparator preparator = new MimeMessagePreparator() {
            @Override
            public void prepare(MimeMessage mimeMessage) throws Exception {
                String[] aTo = TypeCast.isBlank(to) ? null : to.split(",");
                String[] aCc = TypeCast.isBlank(cc) ? null : cc.split(",");
                String[] aBcc = TypeCast.isBlank(bcc) ? null : bcc.split(",");
                MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
                for (final InputStream attachment : attachments) {
                    message.addAttachment("Adjunto" + attachment.hashCode(), new InputStreamSource() {
                        @Override
                        public InputStream getInputStream() throws IOException {
                            return attachment;
                        }
                    });
                }

                if (aTo != null) {
                    message.setTo(aTo);
                }
                if (aCc != null) {
                    message.setCc(aCc);
                }
                if (aBcc != null) {
                    message.setBcc(aBcc);
                }
                message.setFrom(from);
                message.setSubject(subject);
                Map model = new HashMap();
                model.put("body", body);
                String text = FreeMarkerTemplateUtils.processTemplateIntoString(ftl, model);
                message.setText(text, true);
            }
        };
        sender.send(preparator);
        return true;
    }
}
