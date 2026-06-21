package com.banksampah.b2bsales.gui;

import com.banksampah.b2bsales.model.MitraPabrik;
import com.banksampah.b2bsales.model.PenjualanB2B;
import com.banksampah.b2bsales.service.SupabaseService;
import com.formdev.flatlaf.themes.FlatMacLightLaf;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class B2BSalesFrame extends JFrame {
    private final SupabaseService supabaseService;

    private JList<MitraPabrik> listMitra;
    private DefaultListModel<MitraPabrik> listModelMitra;
    private JTable tableTransaksi;
    private DefaultTableModel tableModelTransaksi;

    private JTextField txtItem, txtBerat, txtHarga;

    private JEditorPane txtInvoicePreview;
    private JButton btnSimpanTransaksi;

    public B2BSalesFrame() {
        this.supabaseService = new SupabaseService();
        initUI();
        loadMitraData();
        showInitialWelcomeInvoice();
    }

    private void initUI() {
        setTitle("B2B Sales Management Platform — Pengepul Console");
        setSize(1150, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel rootPanel = new JPanel(new BorderLayout(15, 15));
        rootPanel.setBorder(new EmptyBorder(18, 18, 18, 18));
        rootPanel.setBackground(new Color(245, 247, 250));


        listModelMitra = new DefaultListModel<>();
        listMitra = new JList<>(listModelMitra);
        listMitra.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listMitra.setFixedCellHeight(40);
        listMitra.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        listMitra.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                label.setBorder(new EmptyBorder(0, 12, 0, 12));

                if (value instanceof MitraPabrik m) {
                    if ("AKTIF".equalsIgnoreCase(m.getStatusKontrak()) || "ONLINE".equalsIgnoreCase(m.getStatusKontrak())) {
                        label.setText("<html><body style='width: 100%;'><table width='100%'><tr>"
                                + "<td><b>" + m.getNamaPerusahaan() + "</b></td>"
                                + "<td align='right'><font color='#06d6a0'>● AKTIF</font></td>"
                                + "</tr></table></body></html>");
                    } else {
                        label.setText("<html><body style='width: 100%;'><table width='100%'><tr>"
                                + "<td><font color='#8d99ae'>" + m.getNamaPerusahaan() + "</font></td>"
                                + "<td align='right'><font color='#ef476f'>● NON-AKTIF</font></td>"
                                + "</tr></table></body></html>");
                    }
                }
                return label;
            }
        });

        JScrollPane scrollMitra = new JScrollPane(listMitra);
        scrollMitra.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(220, 225, 230), 1, true),
                " Kontrak Kerja Sama Aktif ", TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 12), new Color(74, 85, 104)
        ));

        listMitra.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && listMitra.getSelectedValue() != null) {
                loadTransaksiMitra(listMitra.getSelectedValue().getId());
            }
        });


        JPanel panelKanan = new JPanel(new BorderLayout(15, 15));
        panelKanan.setOpaque(false);

        String[] columnNames = {"ID Transaksi", "Item Sampah", "Volume / Bobot", "Harga Satuan", "Total Nilai Penjualan"};
        tableModelTransaksi = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        tableTransaksi = new JTable(tableModelTransaksi);
        tableTransaksi.setRowHeight(32);
        tableTransaksi.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tableTransaksi.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tableTransaksi.getTableHeader().setBackground(new Color(230, 235, 245));
        tableTransaksi.setShowVerticalLines(false);

        JScrollPane scrollTable = new JScrollPane(tableTransaksi);
        scrollTable.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(220, 225, 230), 1, true),
                " Log Penjualan Kargo Skala Besar ", TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 12), new Color(74, 85, 104)
        ));

        JPanel panelFormInput = new JPanel(new GridBagLayout());
        panelFormInput.setBackground(Color.WHITE);
        panelFormInput.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(230, 235, 240), 1, true),
                " Input Konsinyasi Manifes Baru ", TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 12), new Color(74, 85, 104)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 15, 8, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtItem = new JTextField();
        txtItem.putClientProperty("JTextField.placeholderText", "Masukkan jenis kargo (Contoh: Botol Plastik PET, Kardus Kering)");
        txtBerat = new JTextField();
        txtBerat.putClientProperty("JTextField.placeholderText", "Masukkan berat bersih angka saja (Mendukung desimal)");
        txtHarga = new JTextField();
        txtHarga.putClientProperty("JTextField.placeholderText", "Tarif harga per Kg sesuai klausul kontrak");

        addFormField(panelFormInput, "Kategori Item:", txtItem, gbc, 0);
        addFormField(panelFormInput, "Berat Bersih (Kg):", txtBerat, gbc, 1);
        addFormField(panelFormInput, "Harga per Kg:", txtHarga, gbc, 2);

        btnSimpanTransaksi = new JButton("Eksekusi Kontrak & Terbitkan Faktur Resmi");
        btnSimpanTransaksi.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnSimpanTransaksi.setBackground(new Color(43, 89, 243));
        btnSimpanTransaksi.setForeground(Color.WHITE);
        btnSimpanTransaksi.setPreferredSize(new Dimension(100, 40));
        btnSimpanTransaksi.setCursor(new Cursor(Cursor.HAND_CURSOR));

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        gbc.insets = new Insets(12, 15, 12, 15);
        panelFormInput.add(btnSimpanTransaksi, gbc);
        btnSimpanTransaksi.addActionListener(e -> eksekusiPenjualanBaru());

        txtInvoicePreview = new JEditorPane();
        txtInvoicePreview.setContentType("text/html");
        txtInvoicePreview.setEditable(false);
        txtInvoicePreview.setBackground(new Color(240, 244, 248));

        JScrollPane scrollInvoice = new JScrollPane(txtInvoicePreview);
        scrollInvoice.setPreferredSize(new Dimension(scrollInvoice.getPreferredSize().width, 320));
        scrollInvoice.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(220, 225, 230), 1, true),
                " Dokumen Faktur Komersial Elektronik (E-Invoice Preview) ", TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 12), new Color(74, 85, 104)
        ));

        JPanel panelKananSub = new JPanel(new BorderLayout(12, 12));
        panelKananSub.setOpaque(false);
        panelKananSub.add(panelFormInput, BorderLayout.NORTH);
        panelKananSub.add(scrollInvoice, BorderLayout.CENTER);

        panelKanan.add(scrollTable, BorderLayout.CENTER);
        panelKanan.add(panelKananSub, BorderLayout.SOUTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollMitra, panelKanan);
        splitPane.setDividerLocation(320);
        splitPane.setDividerSize(6);
        splitPane.setOpaque(false);
        splitPane.setBorder(null);

        rootPanel.add(splitPane, BorderLayout.CENTER);
        getContentPane().add(rootPanel);
    }

    private void addFormField(JPanel panel, String labelText, JTextField textField, GridBagConstraints gbc, int row) {
        textField.setPreferredSize(new Dimension(100, 32));
        gbc.gridy = row;
        gbc.gridx = 0; gbc.weightx = 0.2;
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(new Color(74, 85, 104));
        panel.add(label, gbc);

        gbc.gridx = 1; gbc.weightx = 0.8;
        panel.add(textField, gbc);
    }

    private void showInitialWelcomeInvoice() {
        txtInvoicePreview.setText("""
            <html>
            <body style='font-family: "Segoe UI", sans-serif; text-align: center; padding-top: 50px; color: #94a3b8;'>
                <h2 style='color: #cbd5e1; font-size: 26px; margin-bottom: 5px;'>🖨️</h2>
                <h3 style='color: #64748b; font-weight: 500; margin-top: 0;'>Belum Ada Transaksi Terpilih</h3>
                <p style='font-size: 12px; max-width: 300px; margin: 0 auto; line-height: 1.5;'>
                    Pilih salah satu mitra kontrak aktif di sisi kiri, isi detail kuantitas item kargo, lalu terbitkan invoice resmi.
                </p>
            </body>
            </html>
            """);
    }

    private void loadMitraData() {
        try {
            listModelMitra.clear();
            List<Map<String, Object>> data = supabaseService.fetchData("tb_mitra_pabrik", "select=*");
            for (Map<String, Object> row : data) {
                String id = row.get("id") != null ? row.get("id").toString() : "";
                String nama = row.get("nama_perusahaan") != null ? row.get("nama_perusahaan").toString() : "Tanpa Nama";
                String pjb = row.get("penanggung_jawab") != null ? row.get("penanggung_jawab").toString() : "-";
                String status = row.get("status_kontrak") != null ? row.get("status_kontrak").toString() : "TIDAK_AKTIF";

                MitraPabrik m = new MitraPabrik(id, nama, pjb, status);
                listModelMitra.addElement(m);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Gagal sinkronisasi data Supabase:\n" + ex.getMessage(),
                    "API Integration Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadTransaksiMitra(String idMitra) {
        try {
            tableModelTransaksi.setRowCount(0);
            String filter = "mitra_id=eq." + idMitra;
            List<Map<String, Object>> data = supabaseService.fetchData("tb_penjualan_b2b", filter);
            for (Map<String, Object> row : data) {
                tableModelTransaksi.addRow(new Object[]{
                        "TRX-" + row.get("id").toString().substring(0, 5).toUpperCase(),
                        row.get("item_sampah"),
                        row.get("berat_kg") + " Kg",
                        "Rp " + String.format("%,.0f", Double.parseDouble(row.get("harga_per_kg").toString())),
                        "Rp " + String.format("%,.0f", Double.parseDouble(row.get("total_harga").toString()))
                });
            }

            tableTransaksi.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                    if (!isSelected) {
                        c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 250, 253));
                    }

                    if (column >= 2) {
                        setHorizontalAlignment(JLabel.RIGHT);
                    } else {
                        setHorizontalAlignment(JLabel.LEFT);
                    }

                    ((JComponent) c).setBorder(new EmptyBorder(0, 8, 0, 8));
                    return c;
                }
            });

            tableTransaksi.repaint();
        } catch (Exception ex) {
            System.err.println("Gagal memuat detail transaksi: " + ex.getMessage());
        }
    }

    private void eksekusiPenjualanBaru() {
        MitraPabrik mitraTerpilih = listMitra.getSelectedValue();
        if (mitraTerpilih == null) {
            JOptionPane.showMessageDialog(this, "Silakan pilih salah satu perusahaan mitra kontrak di panel sebelah kiri!",
                    "Aksi Diperlukan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!"AKTIF".equalsIgnoreCase(mitraTerpilih.getStatusKontrak())) {
            JOptionPane.showMessageDialog(this, "Akses ditolak. Kontrak kerja sama dengan mitra yang dipilih berstatus non-aktif.",
                    "Kontrak Tidak Valid", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            String transactionId = UUID.randomUUID().toString();
            String item = txtItem.getText().trim();

            String beratRaw = txtBerat.getText().replaceAll("[^0-9.]", "");
            String hargaRaw = txtHarga.getText().replaceAll("[^0-9.]", "");

            if (item.isEmpty() || beratRaw.isEmpty() || hargaRaw.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Mohon lengkapi seluruh kolom formulir manifes penjualan kargo!",
                        "Validasi Gagal", JOptionPane.WARNING_MESSAGE);
                return;
            }

            double berat = Double.parseDouble(beratRaw);
            double harga = Double.parseDouble(hargaRaw);

            PenjualanB2B transaksi = new PenjualanB2B(transactionId, mitraTerpilih.getId(), item, berat, harga);

            Map<String, Object> payloadPenjualan = new HashMap<>();
            payloadPenjualan.put("id", transaksi.getId());
            payloadPenjualan.put("mitra_id", transaksi.getMitraId());
            payloadPenjualan.put("item_sampah", transaksi.getItemSampah());
            payloadPenjualan.put("berat_kg", transaksi.getBeratKg());
            payloadPenjualan.put("harga_per_kg", transaksi.getHargaPerKg());
            payloadPenjualan.put("total_harga", transaksi.getTotalHarga());

            supabaseService.insertData("tb_penjualan_b2b", payloadPenjualan);

            Map<String, Object> payloadInvoice = new HashMap<>();
            payloadInvoice.put("id", UUID.randomUUID().toString());
            payloadInvoice.put("penjualan_id", transaksi.getId());
            payloadInvoice.put("grand_total", (transaksi.getTotalHarga() + transaksi.hitungPajak(transaksi.getTotalHarga())));
            supabaseService.insertData("tb_invoice_b2b", payloadInvoice);

            txtInvoicePreview.setText(transaksi.generateInvoiceFormat());
            loadTransaksiMitra(mitraTerpilih.getId());

            txtItem.setText(""); txtBerat.setText(""); txtHarga.setText("");
            JOptionPane.showMessageDialog(this, "Manifes Penjualan Sukses Tersimpan & Sinkron ke Supabase!");

        } catch (NumberFormatException nex) {
            JOptionPane.showMessageDialog(this, "Harap masukkan data kuantitas berupa nilai angka desimal yang valid!",
                    "Kesalahan Input", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal mengunggah REST API Supabase:\n" + ex.getMessage(),
                    "Koneksi Bermasalah", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatMacLightLaf());
        } catch (Exception ex) {
            System.err.println("Gagal menginisialisasi tema premium, beralih ke tema default.");
        }

        SwingUtilities.invokeLater(() -> new B2BSalesFrame().setVisible(true));
    }
}
