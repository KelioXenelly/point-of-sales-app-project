import java.awt.Color;
import java.awt.Font;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

/**
 *
 * @author ROG
 */
public class SalesAdmin extends javax.swing.JFrame {
    protected DefaultTableModel model = null;
    protected DefaultTableModel model2 = null;
    protected PreparedStatement stg;
    protected PreparedStatement stm;
    protected PreparedStatement stt;
    protected PreparedStatement sta;
    protected ResultSet rs;
    protected ResultSet rt;
    String query = "SELECT barang.barang_id, kode_barang, nama_barang, detailbarang.qty, "
                 + "detailbarang.harga_beli, detailbarang.harga_jual, detailbarang.profit "
                 + "FROM barang "
                 + "LEFT JOIN kodebarang ON barang.kode_barang_id = kodebarang.kode_barang_id "
                 + "LEFT JOIN detailbarang ON barang.barang_id = detailbarang.barang_id "
                 + "WHERE detailbarang.qty > 0 "
                 + "ORDER BY kode_barang ASC";
    koneksi conn = new koneksi();
    
    /**
     * Creates new form Sales
     */
    public SalesAdmin() {
        initComponents();
        conn.connect();
        if(conn.getConnection() == null) {
            JOptionPane.showMessageDialog(this, "Failed to connect to the database!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            this.stm = conn.getConnection().prepareStatement("SELECT nama_barang FROM barang");
            this.rs = this.stm.executeQuery();
            pilihBarangDropDown.addItem("Pilih sebuah barang");
            while(rs.next()) {
                pilihBarangDropDown.addItem(rs.getString("nama_barang"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(KelolaBarang.class.getName()).log(Level.SEVERE, null, ex);
        }
        currentDate();
        refreshTableBarang();
        refreshTableKeranjang();
    }
    
    class penjualan extends SalesAdmin {
        String barang;
        int qty, barang_id, detail_barang_id;
        double grandTotal = 0;
        double diskon, bayar;
        Date tanggal_jual;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        
        public penjualan() {
            try {
                tanggal_jual = sdf.parse(currentDatelbl.getText());
                barang = pilihBarangDropDown.getSelectedItem().toString();
                qty = Integer.parseInt(jumlahTxt.getText());
                diskon = Double.parseDouble(angkaDiskonTxt.getText().replace("Rp","").trim());
                grandTotal = Double.parseDouble(grandTotalLbl.getText().replace("Rp", "").trim());
                bayar = Double.parseDouble(bayarTxt.getText().replace("Rp", "").trim());
                
                if(bayar >= grandTotal) {
                    this.stm = conn.getConnection().prepareStatement("SELECT barang_id, nama_barang FROM barang");
                    this.rs = this.stm.executeQuery();

                    barang_id = -1; // Default value if no match is found

                    while(this.rs.next()) {
                        if(barang.equals(this.rs.getString("nama_barang"))) {
                            barang_id = this.rs.getInt("barang_id");
                            break; // Exit loop after finding the match
                        }
                    }

                    if (barang_id == -1) {
                        JOptionPane.showMessageDialog(null, "Barang ID not found:");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Nominal bayar tidak boleh < Grand Total");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
        }
    }
    
    public void refreshTableBarang() {
        model = new DefaultTableModel();
        model.addColumn("No");
        model.addColumn("Kode");
        model.addColumn("Nama Barang");
        model.addColumn("Qty");
        model.addColumn("Harga Beli");
        model.addColumn("Harga Jual");
        model.addColumn("Profit");
        tableBarang.setModel(model);
        
        try {
            this.stm = conn.getConnection().prepareStatement(query);
            this.rs = this.stm.executeQuery();
            int i = 1;
            while(rs.next()) {
                Object[]  data =  {
                    i,
                    rs.getString("kode_barang"),
                    rs.getString("nama_barang"),
                    rs.getString("qty") != null ? rs.getString("qty") : "0",
                    rs.getString("harga_beli") != null ? rs.getString("harga_beli") : "0",
                    rs.getString("harga_jual") != null ? rs.getString("harga_jual") : "0",
                    rs.getString("profit") != null ? rs.getString("profit") : "0"
                };
                model.addRow(data);
                i++;
            }
            
            // Close resources
            rs.close();
            stm.close();
            System.out.println("Connection Closed.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        pilihBarangDropDown.setSelectedItem("Pilih sebuah barang");
        jumlahTxt.setText("Masukkan Jumlah Barang");
        diskonTxt.setText("Masukkan Diskon (%)");
        bayarTxt.setText("");
        kembalianLbl.setText("Rp0.0");
    }
    
    public void refreshTableKeranjang() {
        model2 = new DefaultTableModel();
        model2.addColumn("Barang");
        model2.addColumn("Qty");
        model2.addColumn("Harga");
        model2.addColumn("Jumlah");
        tableKeranjang.setModel(model2);
        
        totalLbl.setText("Rp0.0");
        persentaseDiskonLbl.setText("");
        angkaDiskonTxt.setText("Rp0.0");
        grandTotalLbl.setText("Rp0.0");
    }
    
    public void refreshPrice() {
        ArrayList<Double> hargaJual = new ArrayList<>();
        double total = 0;
        double grandTotal = 0;
        
        for (int i = 0; i < model2.getRowCount(); i++) {
            String value = model2.getValueAt(i, 3) != null ? model2.getValueAt(i, 3).toString().trim() : "";
            try {
                if(value.isEmpty() || !value.matches("\\d+(\\.\\d+)?")) {
                    JOptionPane.showMessageDialog(null, "Error: Harga tidak valid di baris " + (i + 1));
                    return;
                }
                double harga = Double.parseDouble(value);
                hargaJual.add(harga);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Error: Harga tidak valid di baris " + (i + 1));
                return;
            }
        }
        
        for (double harga : hargaJual) {
            total += harga;
        }
        
        grandTotal = total;
        
        totalLbl.setText("Rp" + String.valueOf(total));
        grandTotalLbl.setText("Rp" + String.valueOf(grandTotal));
    }
    
    public void addDiskon() {
        double persentaseDiskon = 0;
        double diskon = 0;
        double total = 0;
        double grandTotal = 0;
        
        try {
            total = Double.parseDouble(totalLbl.getText().replace("Rp", "").trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Error: Total harga tidak valid");
            return;
        }
        
        String diskonInput = diskonTxt.getText();
        
        if(!diskonInput.isEmpty()) {
            try {
                if(!diskonInput.matches("^\\d+(.\\d+)?$")) {
                    JOptionPane.showMessageDialog(null, "Error: Persentase Diskon harus berupa angka.");
                    return;
                }
                persentaseDiskon = Double.parseDouble(diskonInput) / 100;
                diskon =  total * persentaseDiskon;
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Error: Persentase diskon tidak valid.");
                return;
            }
        }
        
        grandTotal = total - diskon;
        
        persentaseDiskonLbl.setText(String.valueOf((persentaseDiskon * 100)) + " %");
        angkaDiskonTxt.setText("Rp" + String.valueOf(diskon));
        grandTotalLbl.setText("Rp" + String.valueOf(grandTotal));
    }
    
    public void addPlaceHolderStyle(JTextField textField) {
        Font font = textField.getFont();
        font = font.deriveFont(Font.ITALIC);
        textField.setFont(font);
        textField.setForeground(Color.gray);
    }
    
    public void removePlaceHolderStyle(JTextField textField) {
        Font font = textField.getFont();
        font = font.deriveFont(Font.PLAIN|Font.BOLD);
        textField.setFont(font);
        textField.setForeground(Color.black);
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        currentDatelbl = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        pilihBarangDropDown = new javax.swing.JComboBox<>();
        jLabel12 = new javax.swing.JLabel();
        jumlahTxt = new javax.swing.JTextField();
        minusBtn = new javax.swing.JButton();
        plusBtn = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableBarang = new javax.swing.JTable();
        okBtn = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        kembalianLbl = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jScrollPane3 = new javax.swing.JScrollPane();
        tableKeranjang = new javax.swing.JTable();
        jLabel16 = new javax.swing.JLabel();
        angkaDiskonTxt = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        totalLbl = new javax.swing.JLabel();
        grandTotalLbl = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        persentaseDiskonLbl = new javax.swing.JLabel();
        bayarTxt = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        diskonTxt = new javax.swing.JTextField();
        prosesBtn = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        javax.swing.JLabel jLabel19 = new javax.swing.JLabel();
        salesBtn5 = new javax.swing.JButton();
        kelolaBarangBtn = new javax.swing.JButton();
        pemutihanBtn = new javax.swing.JButton();
        kelolaPenggunaBtn = new javax.swing.JButton();
        logoutBtn5 = new javax.swing.JButton();
        ubahHargaBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
        jPanel7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        currentDatelbl.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        currentDatelbl.setText("Current Date");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel5.setText("Pilih Barang:");

        pilihBarangDropDown.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                pilihBarangDropDownItemStateChanged(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel12.setText("Jumlah:");

        jumlahTxt.setText("Masukkan Jumlah Barang");
        jumlahTxt.setToolTipText("");
        jumlahTxt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jumlahTxtFocusGained(evt);
            }
        });

        minusBtn.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        minusBtn.setText("-");
        minusBtn.setAlignmentY(0.0F);
        minusBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                minusBtnMouseClicked(evt);
            }
        });

        plusBtn.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        plusBtn.setText("+");
        plusBtn.setAlignmentY(0.0F);
        plusBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                plusBtnMouseClicked(evt);
            }
        });

        tableBarang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "No", "Kode", "Nama Barang", "Qty", "Harga Beli", "Harga Jual", "Profit"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableBarang.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        tableBarang.setShowGrid(true);
        tableBarang.getTableHeader().setReorderingAllowed(false);
        tableBarang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableBarangMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tableBarang);
        if (tableBarang.getColumnModel().getColumnCount() > 0) {
            tableBarang.getColumnModel().getColumn(0).setMinWidth(35);
            tableBarang.getColumnModel().getColumn(0).setPreferredWidth(35);
            tableBarang.getColumnModel().getColumn(0).setMaxWidth(35);
            tableBarang.getColumnModel().getColumn(1).setMinWidth(70);
            tableBarang.getColumnModel().getColumn(1).setPreferredWidth(70);
            tableBarang.getColumnModel().getColumn(1).setMaxWidth(70);
            tableBarang.getColumnModel().getColumn(2).setMinWidth(130);
            tableBarang.getColumnModel().getColumn(2).setPreferredWidth(130);
            tableBarang.getColumnModel().getColumn(3).setMinWidth(35);
            tableBarang.getColumnModel().getColumn(3).setPreferredWidth(35);
            tableBarang.getColumnModel().getColumn(3).setMaxWidth(35);
            tableBarang.getColumnModel().getColumn(4).setMinWidth(80);
            tableBarang.getColumnModel().getColumn(4).setPreferredWidth(80);
            tableBarang.getColumnModel().getColumn(4).setMaxWidth(80);
            tableBarang.getColumnModel().getColumn(5).setMinWidth(100);
            tableBarang.getColumnModel().getColumn(5).setPreferredWidth(100);
            tableBarang.getColumnModel().getColumn(5).setMaxWidth(100);
            tableBarang.getColumnModel().getColumn(6).setMinWidth(100);
            tableBarang.getColumnModel().getColumn(6).setPreferredWidth(100);
            tableBarang.getColumnModel().getColumn(6).setMaxWidth(100);
        }

        okBtn.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        okBtn.setText("OK");
        okBtn.setAlignmentY(0.0F);
        okBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                okBtnMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(currentDatelbl, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pilihBarangDropDown, javax.swing.GroupLayout.PREFERRED_SIZE, 220, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addGap(13, 13, 13)
                                .addComponent(jLabel12))
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(jumlahTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(12, 12, 12)
                                .addComponent(minusBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(plusBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(okBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(65, 65, 65)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(plusBtn)
                            .addComponent(minusBtn)))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(currentDatelbl)
                        .addGap(14, 14, 14)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(jLabel12))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(pilihBarangDropDown, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jumlahTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(okBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, 16, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel8.setBackground(new java.awt.Color(103, 136, 201));

        jLabel13.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Kembalian");

        kembalianLbl.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        kembalianLbl.setForeground(new java.awt.Color(255, 255, 255));
        kembalianLbl.setText("Rp500.000");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(kembalianLbl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap(24, Short.MAX_VALUE)
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(kembalianLbl)
                .addGap(28, 28, 28))
        );

        jPanel9.setBackground(new java.awt.Color(255, 255, 255));
        jPanel9.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel15.setText("Keranjang:");

        jSeparator1.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator1.setForeground(new java.awt.Color(0, 0, 0));

        tableKeranjang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"", null, null, null},
                {"", null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Barang", "Qty", "Harga", "Jumlah"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class, java.lang.Double.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableKeranjang.setRowSelectionAllowed(false);
        tableKeranjang.setShowGrid(true);
        tableKeranjang.getTableHeader().setResizingAllowed(false);
        tableKeranjang.getTableHeader().setReorderingAllowed(false);
        jScrollPane3.setViewportView(tableKeranjang);

        jLabel16.setText("Diskon:");

        angkaDiskonTxt.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        angkaDiskonTxt.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        angkaDiskonTxt.setText("Rpxxx");

        jLabel18.setText("Total:");

        totalLbl.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        totalLbl.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        totalLbl.setText("Rp5.150.000");

        grandTotalLbl.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        grandTotalLbl.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        grandTotalLbl.setText("Rp5.150.000");

        jLabel21.setText("Grand Total:");

        persentaseDiskonLbl.setText("2%");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel21)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 121, Short.MAX_VALUE)
                        .addComponent(grandTotalLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, 43, Short.MAX_VALUE)
                            .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(persentaseDiskonLbl, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(totalLbl, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                            .addComponent(angkaDiskonTxt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 153, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(totalLbl))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(angkaDiskonTxt)
                    .addComponent(persentaseDiskonLbl))
                .addGap(8, 8, 8)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21)
                    .addComponent(grandTotalLbl))
                .addContainerGap())
        );

        bayarTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bayarTxtActionPerformed(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel14.setText("Bayar:");

        diskonTxt.setText("Masukkan Diskon (%)");
        diskonTxt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                diskonTxtFocusGained(evt);
            }
        });
        diskonTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                diskonTxtActionPerformed(evt);
            }
        });

        prosesBtn.setText("Proses");
        prosesBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                prosesBtnMouseClicked(evt);
            }
        });

        jPanel10.setBackground(new java.awt.Color(102, 153, 255));

        jLabel17.setFont(new java.awt.Font("sansserif", 0, 14)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setText("Selamat datang,");

        jLabel19.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setText("Admin");

        salesBtn5.setText("Point of Sales");
        salesBtn5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                salesBtn5MouseClicked(evt);
            }
        });

        kelolaBarangBtn.setText("Kelola Barang");
        kelolaBarangBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                kelolaBarangBtnMouseClicked(evt);
            }
        });

        pemutihanBtn.setText("Pemutihan Barang");
        pemutihanBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pemutihanBtnMouseClicked(evt);
            }
        });

        kelolaPenggunaBtn.setText("Kelola Pengguna");
        kelolaPenggunaBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                kelolaPenggunaBtnMouseClicked(evt);
            }
        });

        logoutBtn5.setText("Logout");
        logoutBtn5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                logoutBtn5MouseClicked(evt);
            }
        });

        ubahHargaBtn.setText("Ubah Harga");
        ubahHargaBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ubahHargaBtnMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel17)
                    .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(66, 66, 66)
                .addComponent(salesBtn5, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(kelolaBarangBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pemutihanBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(kelolaPenggunaBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(ubahHargaBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(logoutBtn5)
                .addGap(34, 34, 34))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(jLabel17)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel19))
                    .addGroup(jPanel10Layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(salesBtn5)
                            .addComponent(kelolaBarangBtn)
                            .addComponent(pemutihanBtn)
                            .addComponent(kelolaPenggunaBtn)
                            .addComponent(logoutBtn5)
                            .addComponent(ubahHargaBtn))))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(bayarTxt)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(prosesBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel14)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(diskonTxt, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanel9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
            .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(diskonTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(bayarTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(prosesBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 11, Short.MAX_VALUE))
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    public void logout() {
        JOptionPane.showMessageDialog(null, "You have been logged out.");

        // Create an instance of LoginPanel
        LoginPanel loginPage = new LoginPanel(); 
        loginPage.setVisible(true); // Show the login panel
        loginPage.setLocationRelativeTo(null);
        
        // Hide the current window (Admin/User Panel)
        this.setVisible(false);
        
        // Optional: Dispose of the current window to free up resources
        this.dispose();
    }
    
    public void currentDate() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDateTime now = LocalDateTime.now();
        currentDatelbl.setText(dtf.format(now));
    }
    
    private void plusBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_plusBtnMouseClicked
        try {
            String inputQty = jumlahTxt.getText().trim();
            if (inputQty.isEmpty() || inputQty.equals("Masukkan Jumlah Barang")) {
                jumlahTxt.setText("1");
                return;
            }

            int num = Integer.parseInt(jumlahTxt.getText().trim());
            num++;
            jumlahTxt.setText(String.valueOf(num));

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Tolong masukkan angka yang valid!", "Error", JOptionPane.ERROR_MESSAGE);
            jumlahTxt.setText("1");
        }
    }//GEN-LAST:event_plusBtnMouseClicked

    private void minusBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_minusBtnMouseClicked
        try {
            String inputQty = jumlahTxt.getText().trim();
            if (inputQty.isEmpty() || inputQty.equals("Masukkan Jumlah Barang")) {
                jumlahTxt.setText("1");
                return;
            }
            
            int num = Integer.parseInt(jumlahTxt.getText().trim());
            if(num > 1) {
                num--;
                jumlahTxt.setText(String.valueOf(num));
            } else {
                JOptionPane.showMessageDialog(null, "Angka kuantitas tidak boleh < 1", "Error", JOptionPane.ERROR_MESSAGE);

            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Tolong masukkan angka yang valid!", "Error", JOptionPane.ERROR_MESSAGE);
            jumlahTxt.setText("0");
        }
    }//GEN-LAST:event_minusBtnMouseClicked

    private void okBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_okBtnMouseClicked
        try {
            int selectedRow = tableBarang.getSelectedRow();
            if (selectedRow != -1) {
                int qty = Integer.parseInt(jumlahTxt.getText());
                double harga_jual = Double.parseDouble(model.getValueAt(selectedRow, 5).toString());
                double jumlah_qty = Integer.parseInt(model.getValueAt(selectedRow, 3).toString());
                
                if(qty <= jumlah_qty) {
                    Object[] data = {
                        model.getValueAt(selectedRow, 2),
                        jumlahTxt.getText(),
                        model.getValueAt(selectedRow, 5),
                        qty * harga_jual
                    };
                    model2.addRow(data);

                    refreshPrice();
                } else {
                    JOptionPane.showMessageDialog(null, "Jumlah stok barang tersebut tidak cukup");

                }
            } else {
                JOptionPane.showMessageDialog(null, "Harap pilih baris dalam tabel");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }        
    }//GEN-LAST:event_okBtnMouseClicked

    private void diskonTxtFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_diskonTxtFocusGained
        if(diskonTxt.getText().equals("Masukkan Diskon (%)")) {
            diskonTxt.setText(null);
            diskonTxt.requestFocus();
            // Remove placeholder style
            removePlaceHolderStyle(diskonTxt);
        }
    }//GEN-LAST:event_diskonTxtFocusGained

    private void tableBarangMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableBarangMouseClicked
        try {
            int selectedRow = tableBarang.getSelectedRow();
            if (selectedRow != -1) {
                Object namaBarang = model.getValueAt(selectedRow, 2);
                
                pilihBarangDropDown.setSelectedItem(namaBarang != null ? namaBarang.toString() : "");
            } else {
                JOptionPane.showMessageDialog(null, "No row selected.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }//GEN-LAST:event_tableBarangMouseClicked

    private void jumlahTxtFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jumlahTxtFocusGained
        if(jumlahTxt.getText().equals("Masukkan Jumlah Barang")) {
            jumlahTxt.setText(null);
            jumlahTxt.requestFocus();
            // Remove placeholder style
            removePlaceHolderStyle(jumlahTxt);
        }
    }//GEN-LAST:event_jumlahTxtFocusGained

    private void diskonTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_diskonTxtActionPerformed
        addDiskon();
    }//GEN-LAST:event_diskonTxtActionPerformed

    private void prosesBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_prosesBtnMouseClicked
        try {
            penjualan pjl = new penjualan();
            int selectedRow = tableBarang.getSelectedRow();
            if (selectedRow != -1) {
                Object qty = model.getValueAt(selectedRow, 3);
                Object hargaBeli = model.getValueAt(selectedRow, 4);
                Object hargaJual = model.getValueAt(selectedRow, 5);
                
                int qtyTable = Integer.parseInt(qty.toString());
                double hargaBeliTable = Double.parseDouble(hargaBeli.toString());
                double hargaJualTable = Double.parseDouble(hargaJual.toString());
                
                int qtyNow = qtyTable - pjl.qty;
                double profitNow = (qtyNow * hargaJualTable) - (qtyNow * hargaBeliTable);
                double kembalian = pjl.bayar - pjl.grandTotal;
                
                if(pjl.barang == null || pjl.barang.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Barang belum dipilih!");
                    return;
                }
                if(pjl.qty <= 0) {
                    JOptionPane.showMessageDialog(null, "Qty tidak valid!");
                    return;
                }
                if(pjl.bayar <= 0) {
                    JOptionPane.showMessageDialog(null, "Jumlah bayar tidak valid!");
                    return;
                }
                
                if(!pjl.barang.isEmpty() && pjl.qty != 0 && pjl.bayar != 0) {
                    // Fetch detail_barang_id
                    this.stt = conn.getConnection().prepareStatement("SELECT detail_barang_id, qty, harga_beli "
                                                                   + "FROM detailbarang where barang_id = ?");
                    this.stt.setInt(1, pjl.barang_id);
                    this.rt = this.stt.executeQuery();

                    while(this.rt.next()) {
                        if(qtyTable == this.rt.getInt("qty") && hargaBeliTable == Double.parseDouble(this.rt.getString("harga_beli"))) {
                            pjl.detail_barang_id = this.rt.getInt("detail_barang_id");
                            break;
                        }
                    }

                    // If no match found
                    if (pjl.detail_barang_id == 0) {
                        JOptionPane.showMessageDialog(null, "Data barang tidak ditemukan.");
                        return;
                    }

                    String queryUpdateBarang = "UPDATE detailbarang SET qty = ?, profit = ? WHERE detail_barang_id = ?";
                    this.stm = conn.getConnection().prepareStatement(queryUpdateBarang);
                    this.stm.setInt(1, qtyNow);
                    this.stm.setDouble(2, profitNow);
                    this.stm.setInt(3, pjl.detail_barang_id);
                    this.stm.executeUpdate();

                    String queryPenjualan = "INSERT INTO penjualan (tanggal_jual, barang_id, qty, harga_jual, diskon, "
                            + "total, bayar, kembalian) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                    this.sta = conn.getConnection().prepareStatement(queryPenjualan);
                    this.sta.setDate(1, new java.sql.Date(pjl.tanggal_jual.getTime()));
                    this.sta.setInt(2, pjl.barang_id);
                    this.sta.setInt(3, pjl.qty);
                    this.sta.setDouble(4, Double.parseDouble(hargaJual.toString()));
                    this.sta.setDouble(5, pjl.diskon);
                    this.sta.setDouble(6, pjl.grandTotal);
                    this.sta.setDouble(7, pjl.bayar);
                    this.sta.setDouble(8, kembalian);
                    this.sta.executeUpdate();

                    kembalianLbl.setText("Rp" + kembalian);

                    JOptionPane.showMessageDialog(null, "Transaksi Penjualan Berhasil");
                    Thread.sleep(1000);

                    refreshTableKeranjang();
                    refreshTableBarang();
                } else {
                    JOptionPane.showMessageDialog(null, "Harap masukkan data transaksi secara lengkap");
                }
            } else {
                JOptionPane.showMessageDialog(null, "No row selected.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Database Error: " + e.getMessage());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid number format: " + e.getMessage());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }//GEN-LAST:event_prosesBtnMouseClicked

    private void bayarTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bayarTxtActionPerformed
        try {
            penjualan pjl = new penjualan();
            int selectedRow = tableBarang.getSelectedRow();
            if (selectedRow != -1) {
                Object qty = model.getValueAt(selectedRow, 3);
                Object hargaBeli = model.getValueAt(selectedRow, 4);
                Object hargaJual = model.getValueAt(selectedRow, 5);
                
                int qtyTable = Integer.parseInt(qty.toString());
                double hargaBeliTable = Double.parseDouble(hargaBeli.toString());
                double hargaJualTable = Double.parseDouble(hargaJual.toString());
                
                int qtyNow = qtyTable - pjl.qty;
                double profitNow = (qtyNow * hargaJualTable) - (qtyNow * hargaBeliTable);
                double kembalian = pjl.bayar - pjl.grandTotal;
                
                if(!pjl.barang.isEmpty() && pjl.qty != 0 && pjl.bayar != 0) {
                    // Fetch detail_barang_id
                    this.stt = conn.getConnection().prepareStatement("SELECT detail_barang_id, qty, harga_beli "
                                                                   + "FROM detailbarang where barang_id = ?");
                    this.stt.setInt(1, pjl.barang_id);
                    this.rt = this.stt.executeQuery();

                    while(this.rt.next()) {
                        if(qtyTable == this.rt.getInt("qty") && hargaBeliTable == Double.parseDouble(this.rt.getString("harga_beli"))) {
                            pjl.detail_barang_id = this.rt.getInt("detail_barang_id");
                            break;
                        }
                    }
                    
                    // If no match found
                    if (pjl.detail_barang_id == 0) {
                        JOptionPane.showMessageDialog(null, "No matching detail_barang_id found!");
                        return;
                    }
                    System.out.println("detail_barang_id: " + pjl.detail_barang_id);
                
                    String queryUpdateBarang = "UPDATE detailbarang SET qty = ?, profit = ? WHERE detail_barang_id = ?";
                    String queryPenjualan = "INSERT INTO penjualan (tanggal_jual, barang_id, qty, harga_jual, diskon, "
                            + "total, bayar, kembalian) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                    
                    this.stm = conn.getConnection().prepareStatement(queryUpdateBarang);
                    this.stm.setInt(1, qtyNow);
                    this.stm.setDouble(2, profitNow);
                    this.stm.setInt(3, pjl.detail_barang_id);
                    this.stm.executeUpdate();
                    
                    this.sta = conn.getConnection().prepareStatement(queryPenjualan);
                    this.sta.setDate(1, new java.sql.Date(pjl.tanggal_jual.getTime()));
                    this.sta.setInt(2, pjl.barang_id);
                    this.sta.setInt(3, pjl.qty);
                    this.sta.setDouble(4, Double.parseDouble(hargaJual.toString()));
                    this.sta.setDouble(5, pjl.diskon);
                    this.sta.setDouble(6, pjl.grandTotal);
                    this.sta.setDouble(7, pjl.bayar);
                    this.sta.setDouble(8, kembalian);
                    this.sta.executeUpdate();
                    
                    kembalianLbl.setText("Rp" + kembalian);
                    
                    JOptionPane.showMessageDialog(null, "Transaksi Penjualan Berhasil");
                    Thread.sleep(1000);

                    refreshTableKeranjang();
                    refreshTableBarang();
                } else {
                    JOptionPane.showMessageDialog(null, "Harap masukkan data transaksi secara lengkap");

                }
            } else {
                JOptionPane.showMessageDialog(null, "No row selected.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Database Error: " + e.getMessage());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid number format: " + e.getMessage());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }//GEN-LAST:event_bayarTxtActionPerformed

    private void pilihBarangDropDownItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_pilihBarangDropDownItemStateChanged
        try {
            String selectedItem = pilihBarangDropDown.getSelectedItem().toString();
            int rowCount = tableBarang.getRowCount();
            
            if (rowCount == 0) { // Check if the table is empty
                JOptionPane.showMessageDialog(null, "Table is empty. Please refresh or add items first.");
                return; // Exit the method
            }
            
            int rowIndex = -1;
            
            for (int i = 0; i < rowCount; i++) {
                Object cellValue = tableBarang.getValueAt(i, 2);
                if (cellValue != null) {
                    String namaBarang = cellValue.toString();
                    if (selectedItem.equals(namaBarang)) {
                        rowIndex = i;
                        break;
                    }
                }
            }
            
            if(rowIndex != -1) {
                tableBarang.setRowSelectionInterval(rowIndex, rowIndex);
                tableBarang.scrollRectToVisible(tableBarang.getCellRect(rowIndex, 0, true));
            }
        } catch (IndexOutOfBoundsException e) {
            JOptionPane.showMessageDialog(null, "Error: Row index out of range. Please check the table data.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }//GEN-LAST:event_pilihBarangDropDownItemStateChanged

    private void salesBtn5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_salesBtn5MouseClicked
        SalesAdmin PointofSalesPage = new SalesAdmin();
        PointofSalesPage.setVisible(true);
        PointofSalesPage.setLocationRelativeTo(null);

        this.setVisible(false);
    }//GEN-LAST:event_salesBtn5MouseClicked

    private void kelolaBarangBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_kelolaBarangBtnMouseClicked
        KelolaBarang kelolaBarangPage = new KelolaBarang();
        kelolaBarangPage.setVisible(true);
        kelolaBarangPage.setLocationRelativeTo(null);

        this.setVisible(false);
    }//GEN-LAST:event_kelolaBarangBtnMouseClicked

    private void pemutihanBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pemutihanBtnMouseClicked
        PemutihanBarang pemutihanPage = new PemutihanBarang();
        pemutihanPage.setVisible(true);
        pemutihanPage.setLocationRelativeTo(null);

        this.setVisible(false);
    }//GEN-LAST:event_pemutihanBtnMouseClicked

    private void kelolaPenggunaBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_kelolaPenggunaBtnMouseClicked
        KelolaPengguna kelolaPenggunaPage = new KelolaPengguna();
        kelolaPenggunaPage.setVisible(true);
        kelolaPenggunaPage.setLocationRelativeTo(null);

        this.setVisible(false);
    }//GEN-LAST:event_kelolaPenggunaBtnMouseClicked

    private void logoutBtn5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logoutBtn5MouseClicked
        logout();
    }//GEN-LAST:event_logoutBtn5MouseClicked

    private void ubahHargaBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ubahHargaBtnMouseClicked
        UbahHarga ubahHargaPage = new UbahHarga();
        ubahHargaPage.setVisible(true);
        ubahHargaPage.setLocationRelativeTo(null);

        this.setVisible(false);
    }//GEN-LAST:event_ubahHargaBtnMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(SalesAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SalesAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SalesAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SalesAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SalesAdmin().setVisible(true);
                
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel angkaDiskonTxt;
    private javax.swing.JTextField bayarTxt;
    private javax.swing.JLabel currentDatelbl;
    private javax.swing.JTextField diskonTxt;
    private javax.swing.JLabel grandTotalLbl;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField jumlahTxt;
    private javax.swing.JButton kelolaBarangBtn;
    private javax.swing.JButton kelolaPenggunaBtn;
    private javax.swing.JLabel kembalianLbl;
    private javax.swing.JButton logoutBtn5;
    private javax.swing.JButton minusBtn;
    private javax.swing.JButton okBtn;
    private javax.swing.JButton pemutihanBtn;
    private javax.swing.JLabel persentaseDiskonLbl;
    private javax.swing.JComboBox<String> pilihBarangDropDown;
    private javax.swing.JButton plusBtn;
    private javax.swing.JButton prosesBtn;
    private javax.swing.JButton salesBtn5;
    private javax.swing.JTable tableBarang;
    private javax.swing.JTable tableKeranjang;
    private javax.swing.JLabel totalLbl;
    private javax.swing.JButton ubahHargaBtn;
    // End of variables declaration//GEN-END:variables
}
