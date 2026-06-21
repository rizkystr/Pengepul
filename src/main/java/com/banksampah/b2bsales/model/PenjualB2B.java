package com.banksampah.b2bsales.model;

import com.banksampah.b2bsales.interfaces.Invoicable;
import com.banksampah.b2bsales.interfaces.Taxable;

public class PenjualanB2B implements Taxable, Invoicable {
    private String id;
    private String mitraId;
    private String itemSampah;
    private double beratKg;
    private double hargaPerKg;
    private double totalHarga;

    public PenjualanB2B() {}

    public PenjualanB2B(String id, String mitraId, String itemSampah, double beratKg, double hargaPerKg) {
        this.id = id;
        this.mitraId = mitraId;
        this.itemSampah = itemSampah;
        this.beratKg = beratKg;
        this.hargaPerKg = hargaPerKg;
        this.totalHarga = beratKg * hargaPerKg;
    }

    @Override
    public double hitungPajak(double subTotal) {
        return subTotal * PPN_RATE;
    }

    @Override
    public String generateInvoiceFormat() {
        double subTotal = this.totalHarga;
        double pajak = hitungPajak(subTotal);
        double grandTotal = subTotal + pajak;
        String shortTxId = "TRX-" + id.substring(0, 8).toUpperCase();

        return """
        <html>
        <head>
        <style>
            body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; margin: 0; padding: 10px; background-color: #f8fba0; color: #333333; }
            .invoice-card { background: #ffffff; border-radius: 12px; box-shadow: 0 4px 15px rgba(0,0,0,0.05); padding: 25px; border: 1px solid #eef2f5; }
            .header-table { width: 100%%; margin-bottom: 20px; border-bottom: 2px dashed #e2e8f0; padding-bottom: 15px; }
            .brand-title { font-size: 18px; font-weight: bold; color: #1e3a8a; text-transform: uppercase; letter-spacing: 1px; }
            .meta-text { font-size: 11px; color: #64748b; text-align: right; }
            .details-box { background: #f8fafc; border-radius: 8px; padding: 12px; margin-bottom: 20px; font-size: 12px; border: 1px solid #f1f5f9; }
            .item-table { width: 100%%; border-collapse: collapse; margin-bottom: 25px; font-size: 12px; }
            .item-table th { background: #3b82f6; color: #ffffff; padding: 10px; text-align: left; font-weight: bold; }
            .item-table td { padding: 12px 10px; border-bottom: 1px solid #e2e8f0; color: #475569; }
            .total-table { width: 100%%; font-size: 13px; border-top: 1px solid #cbd5e1; padding-top: 10px; }
            .total-row { font-weight: bold; font-size: 16px; color: #0f172a; }
            .badge { background: #dcfce7; color: #15803d; padding: 4px 8px; border-radius: 6px; font-weight: bold; font-size: 10px; }
        </style>
        </head>
        <body>
            <div class='invoice-card'>
                <table class='header-table'>
                    <tr>
                        <td class='brand-title'>Faktur Penjualan B2B<br><span style='font-size:12px; color:#64748b; font-weight:normal;'>Bank Sampah Digital Platform</span></td>
                        <td class='meta-text'>
                            <b>No Nota:</b> %s<br>
                            <span class='badge'>Lunas / Ter-Emit</span>
                        </td>
                    </tr>
                </table>
                
                <div class='details-box'>
                    <table width='100%%' cellspacing='0' cellpadding='2'>
                        <tr><td width='25%%'><b>ID Kemitraan</b></td><td>: %s</td></tr>
                        <tr><td><b>Kategori Distribusi</b></td><td>: Korporasi Finansial / Pengepul Utama</td></tr>
                    </table>
                </div>

                <table class='item-table'>
                    <thead>
                        <tr>
                            <th>Deskripsi Komoditas Sampah</th>
                            <th align='right'>Volume</th>
                            <th align='right'>Tarif Harga / Kg</th>
                            <th align='right'>Subtotal</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td><b style='color:#0f172a; text-transform:capitalize;'>%s</b></td>
                            <td align='right'>%.2f Kg</td>
                            <td align='right'>Rp %,.2f</td>
                            <td align='right'>Rp %,.2f</td>
                        </tr>
                    </tbody>
                </table>

                <table class='total-table' cellspacing='0' cellpadding='4' align='right' style='width: 50%%;'>
                    <tr>
                        <td align='left' style='color:#64748b;'>Subtotal DPP:</td>
                        <td align='right' style='color:#475569;'>Rp %,.2f</td>
                    </tr>
                    <tr>
                        <td align='left' style='color:#64748b;'>PPN (11%%):</td>
                        <td align='right' style='color:#475569;'>Rp %,.2f</td>
                    </tr>
                    <tr class='total-row'>
                        <td align='left' style='padding-top:10px; border-top: 2px solid #3b82f6;'>TOTAL TAGIHAN:</td>
                        <td align='right' style='padding-top:10px; border-top: 2px solid #3b82f6; color:#2563eb;'>Rp %,.2f</td>
                    </tr>
                </table>
                <div style='clear: both;'></div>
            </div>
        </body>
        </html>
        """.formatted(shortTxId, mitraId, itemSampah, beratKg, hargaPerKg, totalHarga, totalHarga, pajak, grandTotal);
    }

    // Getter & Setter bawaan
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getMitraId() { return mitraId; }
    public void setMitraId(String mitraId) { this.mitraId = mitraId; }
    public String getItemSampah() { return itemSampah; }
    public void setItemSampah(String itemSampah) { this.itemSampah = itemSampah; }
    public double getBeratKg() { return beratKg; }
    public void setBeratKg(double beratKg) { this.beratKg = beratKg; this.totalHarga = this.beratKg * this.hargaPerKg; }
    public double getHargaPerKg() { return hargaPerKg; }
    public void setHargaPerKg(double hargaPerKg) { this.hargaPerKg = hargaPerKg; this.totalHarga = this.beratKg * this.hargaPerKg; }
    public double getTotalHarga() { return totalHarga; }
    public void setTotalHarga(double totalHarga) { this.totalHarga = totalHarga; }
}
