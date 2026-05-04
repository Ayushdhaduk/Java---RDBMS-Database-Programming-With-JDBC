package smilecare;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.basic.BasicComboBoxUI;

public class AppointmentForm extends JFrame {

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

    // ── Fields ─────────────────────────────────────────────────
    private JTextField        nameField, phoneField, dateField, tokenField;
    private JComboBox<String> serviceBox, doctorBox;
    private JLabel            statusLabel;

    // ══════════════════════════════════════════════════════════
    public AppointmentForm() {
        setTitle("SmileCare");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setUndecorated(true);
        setSize(500, 780);
        setLocationRelativeTo(null);
        setShape(new RoundRectangle2D.Double(0, 0, 500, 780, 24, 24));

        // Root
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG_DARK);
        root.add(buildHeader(), BorderLayout.NORTH);
        root.add(buildForm(),   BorderLayout.CENTER);
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
                    0, 0, new Color(20, 80, 160),
                    getWidth(), getHeight(), new Color(0, 170, 160));
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight() + 20, 24, 24);
                g2.dispose();
            }
        };
        h.setPreferredSize(new Dimension(500, 120));
        h.setOpaque(false);

        // Window drag
        WindowMover mover = new WindowMover(this);
        h.addMouseListener(mover);
        h.addMouseMotionListener(mover);

        // Icon + title
        JLabel icon = new JLabel("🦷");
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
        icon.setBounds(26, 26, 46, 46);

        JLabel title = new JLabel("SmileCare");
        title.setFont(F_TITLE);
        title.setForeground(Color.WHITE);
        title.setBounds(82, 28, 200, 32);

        JLabel sub = new JLabel("Dental Appointment System");
        sub.setFont(F_SMALL);
        sub.setForeground(new Color(210, 235, 255, 200));
        sub.setBounds(82, 60, 240, 18);

        // Close ✕
        JButton close = windowBtn("✕", ERROR_COLOR);
        close.setBounds(456, 14, 30, 30);
        close.addActionListener(e -> System.exit(0));

        // Minimise
        JButton min = windowBtn("─", TEXT_MUTED);
        min.setBounds(420, 14, 30, 30);
        min.addActionListener(e -> setState(ICONIFIED));

        h.add(icon); h.add(title); h.add(sub);
        h.add(close); h.add(min);
        return h;
    }

    // ── FORM ───────────────────────────────────────────────────
    private JScrollPane buildForm() {
        JPanel inner = new JPanel();
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));
        inner.setBackground(BG_DARK);
        inner.setBorder(new EmptyBorder(20, 28, 10, 28));

        // Card
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(BG_CARD);
        card.setBorder(new CompoundBorder(
            new LineBorder(BORDER_COLOR, 1, true),
            new EmptyBorder(22, 24, 22, 24)
        ));
        card.setAlignmentX(LEFT_ALIGNMENT);

        // ── Fields ────────────────────────────────────────────
        card.add(label("👤  Patient Name"));
        card.add(vgap(6));
        nameField = textField("Enter full name");
        card.add(nameField);

        card.add(vgap(14));
        card.add(label("📞  Phone Number"));
        card.add(vgap(6));
        phoneField = textField("10-digit mobile number");
        card.add(phoneField);

        card.add(vgap(14));
        card.add(label("📅  Appointment Date  (YYYY-MM-DD)"));
        card.add(vgap(6));
        dateField = textField("e.g. 2026-04-25");
        card.add(dateField);

        card.add(vgap(14));
        card.add(label("🩺  Service Type"));
        card.add(vgap(6));
        serviceBox = combo(new String[]{
            "Teeth Cleaning", "Cavity Filling",
            "Root Canal Treatment", "Teeth Whitening",
            "Braces Consultation", "Tooth Extraction"
        });
        card.add(serviceBox);

        card.add(vgap(14));
        card.add(label("👨‍⚕️  Select Doctor"));
        card.add(vgap(6));
        doctorBox = combo(new String[]{
            "Dr. Priya Sharma", "Dr. Ankit Verma", "Dr. Meera Iyer"
        });
        card.add(doctorBox);

        card.add(vgap(14));
        card.add(label("💸  Advance Token Fee ($)"));
        card.add(vgap(6));
        tokenField = textField("0.00");
        card.add(tokenField);

        // Status
        card.add(vgap(18));
        statusLabel = new JLabel(" ", SwingConstants.CENTER);
        statusLabel.setFont(F_SMALL);
        statusLabel.setForeground(TEXT_MUTED);
        statusLabel.setAlignmentX(CENTER_ALIGNMENT);
        statusLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
        card.add(statusLabel);

        // Book Button
        card.add(vgap(10));
        card.add(bookButton());

        inner.add(card);

        JScrollPane sp = new JScrollPane(inner);
        sp.setOpaque(false);
        sp.getViewport().setOpaque(false);
        sp.setBorder(null);
        sp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        return sp;
    }

    // ── FOOTER ─────────────────────────────────────────────────
    private JPanel buildFooter() {
        JPanel f = new JPanel(new FlowLayout(FlowLayout.CENTER));
        f.setBackground(BG_DARK);
        f.setBorder(new EmptyBorder(0, 0, 16, 0));
        JLabel l = new JLabel("© 2026 SmileCare Dental Clinic");
        l.setFont(F_SMALL);
        l.setForeground(new Color(70, 90, 130));
        f.add(l);
        return f;
    }

    // ── BOOK BUTTON ────────────────────────────────────────────
    private JButton bookButton() {
        JButton btn = new JButton("Book Appointment") {
            boolean hover = false;
            { addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e){ hover=true;  repaint(); }
                public void mouseExited (MouseEvent e){ hover=false; repaint(); }
            }); }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                Color a = hover ? new Color(0, 200, 200) : ACCENT_CYAN;
                Color b = hover ? new Color(30, 140, 255) : ACCENT_BLUE;
                g2.setPaint(new GradientPaint(0, 0, a, getWidth(), 0, b));
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
        btn.setAlignmentX(LEFT_ALIGNMENT);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> handleBooking());
        return btn;
    }

    // ── BOOKING LOGIC ──────────────────────────────────────────
    private void handleBooking() {
        String name    = nameField.getText().trim();
        String phone   = phoneField.getText().trim();
        String date    = dateField.getText().trim();
        String service = serviceBox.getSelectedItem().toString().trim();
        String tokenStr = tokenField.getText().trim();

        // ── Validation ────────────────────────────────────────
        if (name.isEmpty() || name.equals("Enter full name")) {
            status("⚠  Patient name is required.", ERROR_COLOR); return;
        }
        if (!phone.matches("\\d{10}")) {
            status("⚠  Enter a valid 10-digit phone number.", ERROR_COLOR); return;
        }
        if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) {
            status("⚠  Date must be YYYY-MM-DD (e.g. 2026-04-25).", ERROR_COLOR); return;
        }
        
        double parsedToken = 0.0;
        try {
            if (!tokenStr.isEmpty() && !tokenStr.equals("0.00")) {
                parsedToken = Double.parseDouble(tokenStr);
            }
        } catch (NumberFormatException ex) {
            status("⚠  Token fee must be a valid number.", ERROR_COLOR); return;
        }
        final double tokenFee = parsedToken;

        status("⏳  Saving to database...", TEXT_MUTED);

        SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {
            @Override protected String doInBackground() throws Exception {

                // 1️⃣ Insert Patient → get real patient_id
                PatientDAO patientDAO = new PatientDAO();
                Patient patient = new Patient(0, name, phone);
                int patientId = patientDAO.addPatient(patient);

                if (patientId == -1) {
                    return "ERROR: Could not save patient. Check DB connection & table.";
                }

                // 2️⃣ Insert Appointment with correct patient_id and tokenFee mapped to estimatedCost
                AppointmentDAO apptDAO = new AppointmentDAO();
                Appointment appt = new Appointment(patientId, service, date + " 09:00:00", tokenFee);
                int apptId = apptDAO.addAppointment(appt);

                if (apptId == -1) {
                    return "ERROR: Patient saved (ID=" + patientId + ") but appointment failed.";
                }

                // Open Billing Window on success
                return "OK:" + patientId + ":" + apptId;
            }

            @Override protected void done() {
                try {
                    String result = get();
                    if (result.startsWith("OK:")) {
                        String[] parts = result.split(":");
                        int pId = Integer.parseInt(parts[1]);
                        int aId = Integer.parseInt(parts[2]);
                        
                        // Launch Billing Form with tokenFee
                        new BillingForm(pId, aId, name, service, tokenFee);
                        dispose(); // Close appointment window
                    } else {
                        status("❌  " + result.replace("ERROR: ", ""), ERROR_COLOR);
                    }
                } catch (Exception ex) {
                    status("❌  " + ex.getMessage(), ERROR_COLOR);
                    ex.printStackTrace();
                }
            }
        };
        worker.execute();
    }

    // ── HELPERS ────────────────────────────────────────────────
    private void status(String msg, Color c) {
        statusLabel.setText(msg);
        statusLabel.setForeground(c);
    }

    private void clearFields() {
        nameField.setText("");
        phoneField.setText("");
        dateField.setText("");
    }

    private Component vgap(int h) {
        return Box.createRigidArea(new Dimension(0, h));
    }

    private JLabel label(String text) {
        JLabel l = new JLabel(text);
        l.setFont(F_LABEL);
        l.setForeground(TEXT_MUTED);
        l.setAlignmentX(LEFT_ALIGNMENT);
        return l;
    }

    // Styled text field — full width, rounded, dark
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
        tf.setForeground(TEXT_MUTED);
        tf.setCaretColor(ACCENT_BLUE);
        tf.setBorder(new EmptyBorder(9, 14, 9, 14));
        tf.setAlignmentX(LEFT_ALIGNMENT);
        tf.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));

        // Placeholder behaviour
        tf.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (tf.getText().equals(placeholder)) { tf.setText(""); tf.setForeground(TEXT_PRIMARY); }
                tf.repaint();
            }
            public void focusLost(FocusEvent e) {
                if (tf.getText().isEmpty()) { tf.setText(placeholder); tf.setForeground(TEXT_MUTED); }
                tf.repaint();
            }
        });
        return tf;
    }

    // Styled combo box — full width, dark background, matching height
    private JComboBox<String> combo(String[] items) {
        JComboBox<String> cb = new JComboBox<>(items);
        cb.setFont(F_INPUT);
        cb.setForeground(TEXT_PRIMARY);
        cb.setBackground(FIELD_BG);
        cb.setAlignmentX(LEFT_ALIGNMENT);
        cb.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        cb.setPreferredSize(new Dimension(420, 44));

        // Custom UI — dark arrow button, correct background
        cb.setUI(new BasicComboBoxUI() {
            @Override protected JButton createArrowButton() {
                JButton btn = new JButton("▾");
                btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
                btn.setForeground(ACCENT_BLUE);
                btn.setBackground(FIELD_BG);
                btn.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
                btn.setContentAreaFilled(false);
                btn.setFocusPainted(false);
                btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                return btn;
            }
            @Override public void paintCurrentValueBackground(Graphics g, Rectangle bounds, boolean hasFocus) {
                g.setColor(FIELD_BG);
                g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
            }
        });

        // Dark popup list
        cb.setRenderer(new DefaultListCellRenderer() {
            @Override public Component getListCellRendererComponent(
                    JList<?> list, Object value, int idx, boolean sel, boolean focus) {
                JLabel lbl = (JLabel) super.getListCellRendererComponent(list, value, idx, sel, focus);
                lbl.setBackground(sel ? new Color(40, 100, 180) : FIELD_BG);
                lbl.setForeground(TEXT_PRIMARY);
                lbl.setFont(F_INPUT);
                lbl.setBorder(new EmptyBorder(7, 14, 7, 14));
                return lbl;
            }
        });

        // Force popup colors
        Object child = cb.getAccessibleContext().getAccessibleChild(0);
        if (child instanceof JPopupMenu) {
            ((JPopupMenu) child).setBackground(FIELD_BG);
        }
        cb.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1, true));
        return cb;
    }

    // Tiny window control button
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

    // ── WINDOW DRAG ─────────────────────────────────────────────
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

    // ── MAIN ───────────────────────────────────────────────────
    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
        catch (Exception ignored) {}
        SwingUtilities.invokeLater(AppointmentForm::new);
    }
}
