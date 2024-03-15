package Laporan;

import DBConnect.DBConnect;
import com.toedter.calendar.JDateChooser;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;


public class LaporanPenjualan extends JFrame{
    private JPanel Dari;
    private JPanel Hingga;
    private JButton btnLihat;
    public JPanel JPLaporanPenjualan;
    private JPanel JPanel;

    DBConnect connection = new DBConnect();
    JDateChooser datedari = new JDateChooser();
    JDateChooser datehingga = new JDateChooser();
    DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    public LaporanPenjualan(){
        Dari.add(datedari);
        Hingga.add(datehingga);

        btnLihat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (datedari.getDate() == null || datehingga.getDate() == null){
                    JOptionPane.showMessageDialog(null, "Tolong isi tanggal terlebih dahulu !", "Laporan Penjualan",
                            JOptionPane.WARNING_MESSAGE);
                }else {
                    JasperPrint JPrint;
                    String dariTgl = formatter.format(datedari.getDate());
                    String sampaiTgl = formatter.format(datehingga.getDate());
                    Map param = new HashMap();
                    param.put("Dari", dariTgl);
                    param.put("Hingga", sampaiTgl);
                    try {
                        JPrint = JasperFillManager.fillReport("src\\Laporan\\laporanPenjualan.jasper", param, connection.conn);
                        JasperViewer viewer = new JasperViewer(JPrint, false);
                        viewer.setVisible(true);
                    } catch (JRException ex) {
                        JOptionPane.showMessageDialog(null, "Error report : " + ex.getMessage());
                    }
                    datedari.setDate(null);
                    datehingga.setDate(null);
                }
            }
        });
    }

    public static void main(String[] args) {
        LaporanPenjualan frame = new LaporanPenjualan();
        frame.setContentPane(new LaporanPenjualan().JPLaporanPenjualan);
        frame.setExtendedState(MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
