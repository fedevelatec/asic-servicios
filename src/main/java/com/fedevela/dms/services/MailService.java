package com.fedevela.dms.services;

/**
 * Created by fvelazquez on 9/04/14.
 */
import freemarker.template.Configuration;
import freemarker.template.Template;
import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public interface MailService {

    /**
     *
     * @param from
     * @param to
     * @param cc
     * @param bcc
     * @param subject
     * @param body
     * @return
     */
    public boolean send(final String from, final String[] to, final String[] cc, final String[] bcc, final String subject, final String body);

    public boolean send(final String from, final String to, final String cc, final String bcc, final String subject, final String body);

    public boolean send(final String from, final String to, final String cc, final String bcc, final String subject, final List<Map<String, Object>> body, final Template ftl);

    public boolean send(final String from, final String to, final String cc, final String bcc, final String subject, final List<Map<String, Object>> body, final Template ftl, final String attachmentFilename, final InputStream attachment);

    /**
     *
     * @param from
     * @param to
     * @param cc
     * @param bcc
     * @param subject
     * @param body
     * @param template
     * @return
     */
    public boolean send(final String from, final String[] to, final String[] cc, final String[] bcc, final String subject, final String body, final Configuration cfg, final String template);

    public boolean send(final String from, final String to, final String cc, final String bcc, final String subject, final Map<String, Object> body, final Template ftl, final File attachment);

    public boolean send(final String from, final String to, final String cc, final String bcc, final String subject, final List<Map<String, Object>> body, final Template ftl, final File attachment);

    public boolean send(final String from, final String to, final String cc, final String bcc, final String subject, final List<Map<String, Object>> body, final Template ftl, final File[] attachment);

    public boolean send(final String from, final String to, final String cc, final String bcc, final String subject, final List<Map<String, Object>> body, final Template ftl, final InputStream[] attachments);
}
