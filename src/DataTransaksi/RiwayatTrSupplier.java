package DataTransaksi;

import DBConnect.DBConnect;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class RiwayatTrSupplier {
    public JPanel JpRiwayatTrSupplier;
    private JTable tabelHandphone;
    private JTable tabelAksesoris;
    private JTable tabelTransaksi;
    private JRadioButton rbSudah;
    private JPanel PanelKiri;
    private JPanel PanelKanan;
    private JRadioButton rbTidak;
    private JButton btnKonfirmasi;

    String idtr;

    DBConnect connection = new DBConnect();
    private DefaultTableModel model = new DefaultTableModel();
    private DefaultTableModel model2 = new DefaultTableModel();
    private DefaultTableModel model3 = new DefaultTableModel();

    public RiwayatTrSupplier(){
        radiobutton();
        FrameRadioButton();
        model = new DefaultTableModel();
        tabelTransaksi.setModel(model);
        addColomnTransaksi();

        model2 = new DefaultTableModel();
        tabelHandphone.setModel(model2);
        addColomnHandphone();

        model3 = new DefaultTableModel();
        tabelAksesoris.setModel(model3);
        addColomnAksesoris();

        tabelTransaksi.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int i = tabelTransaksi.getSelectedRow();
                idtr = (String) model.getValueAt(i, 0);
                loadDataAksesoris();
                loadDataHandphone();
            }
        });
        rbSudah.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                idtr = null;
                loadDataTransaksiSudah();
            }
        });
        rbTidak.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                idtr = null;
                loadDataTransaksiBelum();
            }
        });

        btnKonfirmasi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = model2.getRowCount();
                int row2 = model3.getRowCount();
                String idhp, idaks;
                if (rbSudah.isSelected()) {
                    JOptionPane.showMessageDialog(null, "Transaksi Sudah Terkonfirmasi", "Informasi",
                            JOptionPane.INFORMATION_MESSAGE);
                }else if (idtr == null){
                    JOptionPane.showMessageDialog(null, "Pilih transaksi yang ingin dikonfirmasi !", "Informasi",
                            JOptionPane.INFORMATION_MESSAGE);
                }else {
                    try {
                        String query = "EXEC sp_UpdateTrSupplier @Id_TrSupplier=?";
                        connection.pstat = connection.conn.prepareStatement(query);
                        connection.pstat.setString(1, idtr);
                        connection.pstat.executeUpdate();
                        connection.pstat.close();

                        for (int i = 0; i < row; i++) {
                            idhp = (String) model2.getValueAt(i, 1);
                            String sql2 = "EXEC sp_UpdateTrsHandphone @Id_TrSupplier=?,@Id_Handphone=?";
                            connection.pstat = connection.conn.prepareStatement(sql2);
                            connection.pstat.setString(1, idtr);
                            connection.pstat.setString(2, idhp);
                            connection.pstat.executeUpdate();
                            connection.pstat.close();
                        }

                        for (int i = 0; i < row2; i++) {
                            idaks = (String) model3.getValueAt(i, 1);
                            String sql3 = "EXEC sp_UpdateTrsAksesoris @Id_TrSupplier=?,@Id_Aksesoris=?";
                            connection.pstat = connection.conn.prepareStatement(sql3);
                            connection.pstat.setString(1, idtr);
                            connection.pstat.setString(2, idaks);
                            connection.pstat.executeUpdate();
                            connection.pstat.close();
                        }

                        JOptionPane.showMessageDialog(null, "Transaksi Berhasil Terkonfirmasi", "Informasi",
                                JOptionPane.INFORMATION_MESSAGE);
                        DefaultTableModel model = (DefaultTableModel) tabelTransaksi.getModel();
                        model.setRowCount(0);
                        DefaultTableModel model2 = (DefaultTableModel) tabelHandphone.getModel();
                        model2.setRowCount(0);
                        DefaultTableModel model3 = (DefaultTableModel) tabelAksesoris.getModel();
                        model3.setRowCount(0);
                        rbSudah.setSelected(false);
                        rbTidak.setSelected(false);
                    } catch (Exception ex) {
                        System.out.println("Error saat update" + ex);
                    }
                }
            }
        });
    }

    public void loadDataTransaksiBelum(){
        model.getDataVector().removeAllElements();
        model.fireTableDataChanged();
        PreparedStatement st;
        try {
            DBConnect connection = new DBConnect();
            connection.stat = connection.conn.createStatement();
            st = connection.conn.prepareStatement("EXEC sp_LoadTransaksiSupplier");
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Object[] obj = new Object[5];
                obj[0] = rs.getString(1);
                obj[1] = rs.getString(2);
                obj[2] = rs.getString(3);
                obj[3] = rs.getInt(4);
                obj[4] = rs.getDate(5);
                model.addRow(obj);
            }
        } catch (Exception ex) {
            System.out.println("Terjadi error saat load data" + ex);
        }
    }

    public void loadDataTransaksiSudah(){
        model.getDataVector().removeAllElements();
        model.fireTableDataChanged();
        PreparedStatement st;
        try {
            DBConnect connection = new DBConnect();
            connection.stat = connection.conn.createStatement();
            st = connection.conn.prepareStatement("EXEC sp_LoadTransaksiSupplier2");
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Object[] obj = new Object[5];
                obj[0] = rs.getString(1);
                obj[1] = rs.getString(2);
                obj[2] = rs.getString(3);
                obj[3] = rs.getInt(4);
                obj[4] = rs.getDate(5);
                model.addRow(obj);
            }
        } catch (Exception ex) {
            System.out.println("Terjadi error saat load data" + ex);
        }
    }

    public void loadDataAksesoris() {
        model3.getDataVector().removeAllElements();
        model3.fireTableDataChanged();
        PreparedStatement st;
        try {
            DBConnect connection = new DBConnect();
            connection.stat = connection.conn.createStatement();
            st = connection.conn.prepareStatement("EXEC sp_LoadDetailSupplierAks @Id_TrSupplier=?");
            st.setString(1,idtr);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Object[] obj = new Object[5];
                obj[0] = rs.getString(1);
                obj[1] = rs.getString(2);
                obj[2] = rs.getString(3);
                model3.addRow(obj);
            }
        } catch (Exception ex) {
            System.out.println("Terjadi error saat load data" + ex);
        }
    }

    public void loadDataHandphone() {
        model2.getDataVector().removeAllElements();
        model2.fireTableDataChanged();
        PreparedStatement st;
        try {
            DBConnect connection = new DBConnect();
            connection.stat = connection.conn.createStatement();
            st = connection.conn.prepareStatement("EXEC sp_LoadDetailSupplierHp @Id_TrSupplier=?");
            st.setString(1,idtr);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Object[] obj = new Object[5];
                obj[0] = rs.getString(1);
                obj[1] = rs.getString(2);
                obj[2] = rs.getString(3);
                model2.addRow(obj);
            }
        } catch (Exception ex) {
            System.out.println("Terjadi error saat load data" + ex);
        }
    }

    public void  addColomnTransaksi() {
        model.addColumn("ID Transaksi");
        model.addColumn("Nama Karyawan");
        model.addColumn("Nama Supplier");
        model.addColumn("Grand Total");
        model.addColumn("Tanggal Pembelian");
    }

    public void addColomnHandphone () {
        model2.addColumn("Id Transaksi");
        model2.addColumn("Id Handphone");
        model2.addColumn("Jumlah");
    }

    public void addColomnAksesoris () {
        model3.addColumn("Id Transaksi");
        model3.addColumn("Id Aksesoris");
        model3.addColumn("Jumlah");
    }

    public void radiobutton(){
        if (rbSudah.isSelected()){
            rbTidak.setSelected(false);
            rbTidak.enable(false);
        }else{
            rbSudah.setSelected(false);
            rbSudah.enable(false);
        }
    }

    public void FrameRadioButton(){
        ButtonGroup grup = new ButtonGroup();
        grup.add(rbSudah);
        grup.add(rbTidak);
    }
}
