package smilecare;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;
import javax.swing.border.*;

public class BillingForm extends JFrame {

    // ── Colours ────────────────────────────────────────────────
    private static final Color BG_DARK       = new Color(13,  17,  30);
    private static final Color BG_CARD       = new Color(22,  28,  48);
    private static final Color ACCENT_BLUE   = new Color(64,  169, 255);
    private static final Color ACCENT_CYAN   = new Color(0,   212, 200);
    private static final Color TEXT_PRIMARY  = new Color(230, 235, 255);
    private static final Color TEXT_MUTED    = new Color(120, 135, 170);
    private static final Color FIELD_BG      = new Color(30,  38,  60);
    private static final Color BORDER_COLOR  = new Color(45,  55,  85);
    private static final Color BORDER_FOCUS  = new Color(64,  169, 255);
    private static final Color SUCCESS_COLOR = new Color(0,   200, 130);
    private static final Color ERROR_COLOR   = new Color(255, 80,  100);

    // ── Fonts ──────────────────────────────────────────────────
    private static final Font F_TITLE  = new Font("Segoe UI", Font.BOLD,  22);
    private static final Font F_LABEL  = new Font("Segoe UI", Font.BOLD,  12);
    private static final Font F_INPUT  = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font F_BTN    = new Font("Segoe UI", Font.BOLD,  14);
    private static final Font F_SMALL  = new Font("Segoe UI", Font.PLAIN, 11);
    private static final Font F_MONO   = new Font("Consolas", Font.PLAIN, 12);

    // ── Fields ─────────────────────────────────────────────────
    private JTextField txtTreatment, txtMedicine, txtTax, txtDiscount;
    private JTextArea invoiceArea;
    private JLabel statusLabel;

    // Passed Context
    private int patientId;
    private int appointmentId;
    private String patientName;
    private String serviceType;
    private double tokenFee;
    private String generatedInvoiceText = null;

    // ══════════════════════════════════════════════════════════
    public BillingForm(int patientId, int appointmentId, String patientName, String serviceType, double tokenFee) {
        this.patientId = patientId;
        this.appointmentId = appointmentId;
        this.patientName = patientName;
        this.serviceType = serviceType;
        this.tokenFee = tokenFee;

        setTitle("SmileCare - Billing");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setUndecorated(true);
        setSize(800, 600); // Wider for invoice panel
        setLocationRelativeTo(null);
        setShape(new RoundRectangle2D.Double(0, 0, 800, 600, 24, 24));

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG_DARK);
        root.add(buildHeader(), BorderLayout.NORTH);
        
        // Split UI: Left (Inputs) | Right (Invoice display Component)
        JPanel contentArea = new JPanel(new GridLayout(1, 2, 20, 0));
        contentArea.setBackground(BG_DARK);
        contentArea.setBorder(new EmptyBorder(20, 28, 10, 28));
        contentArea.add(buildInputPanel());
        contentArea.add(buildInvoicePanel());
        
        root.add(contentArea, BorderLayout.CENTER);
        root.add(buildFooter(), BorderLayout.SOUTH);

        setContentPane(root);
        setVisible(true);
    }

    // ── HEADER ─────────────────────────────────────────────────
    private JPanel buildHeader() {
        JPanel h = new JPanel(null) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(
                    0, 0, new Color(120, 40, 160),
                    getWidth(), getHeight(), new Color(200, 80, 160));
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight() + 20, 24, 24);
                g2.dispose();
            }
        };
        h.setPreferredSize(new Dimension(800, 100));
        h.setOpaque(false);

        // Window drag
        WindowMover mover = new WindowMover(this);
        h.addMouseListener(mover);
        h.addMouseMotionListener(mover);

        // Icon + title
        JLabel icon = new JLabel("💳");
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
        icon.setBounds(26, 16, 46, 46);

        JLabel title = new JLabel("Billing & Invoicing");
        title.setFont(F_TITLE);
        title.setForeground(Color.WHITE);
        title.setBounds(82, 18, 300, 32);

        JLabel sub = new JLabel("Patient: " + patientName + " | Service: " + serviceType + " | Advance: $" + String.format("%.2f", tokenFee));
        sub.setFont(F_SMALL);
        sub.setForeground(new Color(255, 235, 255, 220));
        sub.setBounds(82, 50, 600, 18);

        // Close ✕
        JButton close = windowBtn("✕", ERROR_COLOR);
        close.setBounds(756, 14, 30, 30);
        close.addActionListener(e -> {
            new AppointmentForm();
            this.dispose();
        });

        h.add(icon); h.add(title); h.add(sub); h.add(close);
        return h;
    }

    // ── LEFT: INPUT FORM ───────────────────────────────────────
    private JPanel buildInputPanel() {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(BG_CARD);
        card.setBorder(new CompoundBorder(
            new LineBorder(BORDER_COLOR, 1, true),
            new EmptyBorder(22, 24, 22, 24)
        ));

        card.add(label("💰  Treatment Cost ($)"));
        card.add(vgap(6));
        txtTreatment = textField("0.00");
        card.add(txtTreatment);
        card.add(vgap(14));

        card.add(label("💊  Medication Cost ($)"));
        card.add(vgap(6));
        txtMedicine = textField("0.00");
        card.add(txtMedicine);
        card.add(vgap(14));

        card.add(label("⚖️  Tax Amount ($)"));
        card.add(vgap(6));
        txtTax = textField("0.00");
        card.add(txtTax);
        card.add(vgap(14));

        card.add(label("🛡️  Insurance/Discount ($)"));
        card.add(vgap(6));
        txtDiscount = textField("0.00");
        card.add(txtDiscount);
        card.add(vgap(24));

        // Generate Invoice Button
        JButton btnGen = processButton("Preview Invoice", ACCENT_BLUE, new Color(30, 140, 255));
        btnGen.addActionListener(e -> generatePreview());
        card.add(btnGen);
        
        card.add(vgap(10));
        
        statusLabel = new JLabel(" ", SwingConstants.CENTER);
        statusLabel.setFont(F_SMALL);
        statusLabel.setForeground(TEXT_MUTED);
        statusLabel.setAlignmentX(CENTER_ALIGNMENT);
        card.add(statusLabel);

        return card;
    }

    // ── RIGHT: INVOICE REPORT ──────────────────────────────────
    private JPanel buildInvoicePanel() {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(new Color(250, 250, 252)); // Paper white colour for Invoice
        card.setBorder(new CompoundBorder(
            new LineBorder(new Color(200, 200, 210), 1, true),
            new EmptyBorder(14, 14, 14, 14)
        ));

        // Note: assessment says "Exports invoice to GUI component" -> A JTextArea perfectly fits this.
        invoiceArea = new JTextArea("Invoice preview will appear here...");
        invoiceArea.setEditable(false);
        invoiceArea.setFont(F_MONO);
        invoiceArea.setBackground(new Color(250, 250, 252));
        invoiceArea.setForeground(new Color(30, 30, 40));
        invoiceArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scroll = new JScrollPane(invoiceArea);
        scroll.setBorder(null);

        // Save & Finish Button
        JButton btnSave = processButton("Save & Export Invoice", SUCCESS_COLOR, new Color(0, 160, 100));
        btnSave.addActionListener(e -> finalizeBilling());

        card.add(new JLabel("📋 Invoice Document"), BorderLayout.NORTH);
        card.add(scroll, BorderLayout.CENTER);
        card.add(btnSave, BorderLayout.SOUTH);

        return card;
    }
    
    // ── FOOTER ─────────────────────────────────────────────────
    private JPanel buildFooter() {
        JPanel f = new JPanel(new FlowLayout(FlowLayout.CENTER));
        f.setBackground(BG_DARK);
        f.setBorder(new EmptyBorder(0, 0, 16, 0));
        JLabel l = new JLabel("SmileCare Module 7 - RDBMS & Database Programming");
        l.setFont(F_SMALL);
        l.setForeground(new Color(70, 90, 130));
        f.add(l);
        return f;
    }

    // ── PREVIEW LOGIC ──────────────────────────────────────────
    private void generatePreview() {
        try {
            double trt = parseDouble(txtTreatment.getText());
            double med = parseDouble(txtMedicine.getText());
            double tax = parseDouble(txtTax.getText());
            double dis = parseDouble(txtDiscount.getText());

            generatedInvoiceText = InvoiceGenerator.generateInvoice(
                patientName, serviceType, trt, med, tax, dis, tokenFee
            );
            
            invoiceArea.setText(generatedInvoiceText);
            status("✅ Preview generated.", SUCCESS_COLOR);
            
        } catch (NumberFormatException ex) {
            status("⚠ Please enter valid numerical amounts.", ERROR_COLOR);
        }
    }

    // ── FINALIZE LOGIC ─────────────────────────────────────────
    private void finalizeBilling() {
        if (generatedInvoiceText == null || generatedInvoiceText.isEmpty()) {
            status("⚠ Please click 'Preview Invoice' first.", ERROR_COLOR);
            return;
        }

        try {
            double trt = parseDouble(txtTreatment.getText());
            double med = parseDouble(txtMedicine.getText());
            double tax = parseDouble(txtTax.getText());
            double dis = parseDouble(txtDiscount.getText());

            status("⏳ Saving to database...", TEXT_MUTED);

            SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
                @Override protected Boolean doInBackground() throws Exception {
                    
                    // 1. Calculate total using Billing Model
                    Billing b = new Billing(appointmentId, trt, med, tax, dis);
                    
                    // 2. Persist to DB (using RDBMS/JDBC as required)
                    BillingDAO dao = new BillingDAO();
                    dao.addBilling(b); // We won't strictly capture generated bill_id here for now

                    // 3. Persist to File
                    FileSave.saveToFile(generatedInvoiceText);
                    
                    return true;
                }

                @Override protected void done() {
                    try {
                        if (get()) {
                            JOptionPane.showMessageDialog(BillingForm.this, 
                                "Invoice successfully generated, saved to DB, and exported to file!", 
                                "Billing Complete", JOptionPane.INFORMATION_MESSAGE);
                            
                            // Return to Main Form
                            new AppointmentForm();
                            dispose();
                        }
                    } catch (Exception ex) {
                        status("❌ Database error: " + ex.getMessage(), ERROR_COLOR);
                    }
                }
            };
            worker.execute();

        } catch (Exception ex) {
            status("⚠ Validation error.", ERROR_COLOR);
        }
    }

    // ── HELPERS ────────────────────────────────────────────────
    private double parseDouble(String val) {
        if (val == null || val.trim().isEmpty() || val.equals("0.00")) return 0.0;
        return Double.parseDouble(val.replace(",", "").trim());
    }

    private void status(String msg, Color c) {
        statusLabel.setText(msg);
        statusLabel.setForeground(c);
    }

    private Component vgap(int h) { return Box.createRigidArea(new Dimension(0, h)); }

    private JLabel label(String text) {
        JLabel l = new JLabel(text);
        l.setFont(F_LABEL);
        l.setForeground(TEXT_MUTED);
        l.setAlignmentX(LEFT_ALIGNMENT);
        return l;
    }

    private JTextField textField(String placeholder) {
        JTextField tf = new JTextField(placeholder) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(FIELD_BG);
                g2.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 10, 10);
                g2.setColor(hasFocus() ? BORDER_FOCUS : BORDER_COLOR);
                g2.setStroke(new BasicStroke(hasFocus() ? 1.8f : 1f));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 10, 10);
                super.paintComponent(g);
                g2.dispose();
            }
        };
        tf.setOpaque(false);
        tf.setFont(F_INPUT);
        tf.setForeground(TEXT_PRIMARY);
        tf.setCaretColor(ACCENT_BLUE);
        tf.setBorder(new EmptyBorder(9, 14, 9, 14));
        tf.setAlignmentX(LEFT_ALIGNMENT);
        tf.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));

        tf.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (tf.getText().equals(placeholder)) { tf.setText(""); }
                tf.repaint();
            }
            public void focusLost(FocusEvent e) {
                if (tf.getText().isEmpty()) { tf.setText(placeholder); }
                tf.repaint();
            }
        });
        return tf;
    }

    private JButton processButton(String text, Color base, Color hoverColor) {
        JButton btn = new JButton(text) {
            boolean hover = false;
            { addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e){ hover=true;  repaint(); }
                public void mouseExited (MouseEvent e){ hover=false; repaint(); }
            }); }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(hover ? hoverColor : base);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.setFont(F_BTN);
                g2.setColor(Color.WHITE);
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(),
                    (getWidth()  - fm.stringWidth(getText())) / 2,
                    (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }
        };
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        btn.setAlignmentX(CENTER_ALIGNMENT);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JButton windowBtn(String text, Color fg) {
        JButton btn = new JButton(text) {
            boolean h = false;
            { addMouseListener(new MouseAdapter(){
                public void mouseEntered(MouseEvent e){ h=true;  repaint(); }
                public void mouseExited (MouseEvent e){ h=false; repaint(); }
            }); }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (h) { g2.setColor(new Color(255,255,255,30)); g2.fillOval(0,0,getWidth(),getHeight()); }
                g2.setFont(new Font("Segoe UI", Font.BOLD, 13));
                g2.setColor(fg);
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(),
                    (getWidth()-fm.stringWidth(getText()))/2,
                    (getHeight()+fm.getAscent()-fm.getDescent())/2);
                g2.dispose();
            }
        };
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    static class WindowMover extends MouseAdapter {
        private Point start; private final Window w;
        WindowMover(Window w){ this.w = w; }
        public void mousePressed (MouseEvent e){ start = e.getPoint(); }
        public void mouseDragged (MouseEvent e){
            if(start==null) return;
            Point l = w.getLocation();
            w.setLocation(l.x+e.getX()-start.x, l.y+e.getY()-start.y);
        }
    }
}
