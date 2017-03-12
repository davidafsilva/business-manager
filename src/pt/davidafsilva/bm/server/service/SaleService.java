package pt.davidafsilva.bm.server.service;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

import de.congrace.exp4j.Calculable;
import de.congrace.exp4j.ExpressionBuilder;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import pt.davidafsilva.bm.server.dao.interfaces.ISaleDAO;
import pt.davidafsilva.bm.server.service.interfaces.IConfigurationService;
import pt.davidafsilva.bm.server.service.interfaces.ISaleService;
import pt.davidafsilva.bm.shared.domain.Client;
import pt.davidafsilva.bm.shared.domain.Configuration;
import pt.davidafsilva.bm.shared.domain.Product;
import pt.davidafsilva.bm.shared.domain.Sale;
import pt.davidafsilva.bm.shared.domain.SaleProduct;
import pt.davidafsilva.bm.shared.domain.User;
import pt.davidafsilva.bm.shared.enums.ConfigurationKey;
import pt.davidafsilva.bm.shared.exception.ApplicationException;


/**
 * SaleService.java
 *
 * The sale service (business code) implementation
 *
 * @author David Silva <david@davidafsilva.pt>
 * @date 6:16:18 PM
 */
public class SaleService implements ISaleService {

  // logger
  private final Logger log = Logger.getLogger(SaleService.class);

  private final ISaleDAO saleDAO = ISaleDAO.Util.getInstance();

  private final IConfigurationService configService = IConfigurationService.Util.getInstance();


  /* (non-Javadoc)
   * @see pt.davidafsilva.bm.server.service.interfaces.ISaleService#searchSales
   * (pt.davidafsilva.bm.shared.domain.Client, java.util.Date, java.util.Date)
   */
  @Override
  public List<Sale> searchSales(User seller, Client client, Date initialDate, Date finalDate) {
    List<Sale> sales = null;
    log.debug("searchSales(): searching for sales.. ");
    try {
      sales = saleDAO.searchSales(seller, client, initialDate, finalDate);

      log.debug("searchSales(): sales searched sucessfully");
    } catch (RuntimeException | SQLException e) {
      log.error("searchSales(): error searching for sales", e);
    }

    return sales;
  }

  /* (non-Javadoc)
   * @see pt.davidafsilva.bm.server.service.interfaces.ISaleService#save(pt.davidafsilva.bm.shared.domain.Sale)
   */
  @Override
  public void save(Sale sale) throws ApplicationException {
    log.debug("save(): saving the sale.. ");
    try {
      // update totals
      updateSaleTotals(sale);

      // save
      saleDAO.save(sale);

      log.debug("save(): sale saved sucessfully");
    } catch (RuntimeException | SQLException e) {
      log.error("save(): error saving the sale", e);
      throw new ApplicationException("Ocorreu um erro a gravar a venda.");
    }
  }

  /* (non-Javadoc)
   * @see pt.davidafsilva.bm.server.service.interfaces.ISaleService#markSaleHasSentByEmail(pt.davidafsilva.bm.shared.domain.Sale)
   */
  @Override
  public void markSaleHasSentByEmail(Sale sale) throws ApplicationException {
    log.debug("markSaleHasSentByEmail(): updating the sale.. ");
    try {
      // save
      saleDAO.setSentByEmail(sale.getId());
      sale.setSentByEmail(true);

      log.debug("markSaleHasSentByEmail(): sale updated sucessfully");
    } catch (RuntimeException | SQLException e) {
      log.error("markSaleHasSentByEmail(): error updating the sale", e);
      throw new ApplicationException("Ocorreu um erro a gravar a venda.");
    }
  }

  /**
   * Update the sale totals
   *
   * @param sale The sale to update
   */
  private void updateSaleTotals(Sale sale) {
    double t_liquid, t_brute, t_vat, t_discount;
    t_liquid = t_brute = t_vat = t_discount = 0;

    double liquid, brute, vat, discount;
    for (SaleProduct product : sale.getProducts()) {
      brute = product.getPrice() == null ? 0 : product.getPrice() * product.getAmount();

      // VAT
      if (product.getVat() != null && product.getVat() > 0) {
        vat = brute * (product.getVat() / 100);
      } else {
        vat = 0;
      }

      // liquid
      liquid = brute + vat;

      // discount
      if (product.getDiscount() != null && !product.getDiscount().trim().isEmpty()) {
        try {
          Calculable calc = new ExpressionBuilder(product.getDiscount()).build();
          discount = liquid * (calc.calculate() / 100);
        } catch (Exception e) {
          discount = 0;
        }
      } else {
        discount = 0;
      }

      // liquid
      liquid -= discount;

      // update values
      t_brute += brute;
      t_vat += vat;
      t_discount += discount;
      t_liquid += liquid;
    }

    sale.setTotalBrute(t_brute < 0 ? 0 : t_brute);
    sale.setTotalLiquid(t_liquid < 0 ? 0 : t_liquid);
    sale.setTotalDiscount(t_discount < 0 ? 0 : t_discount);
    sale.setTotalVat(t_vat < 0 ? 0 : t_vat);
  }

  /* (non-Javadoc)
   * @see pt.davidafsilva.bm.server.service.interfaces.ISaleService#generateReport
   * (pt.davidafsilva.bm.shared.domain.Sale)
   */
  @SuppressWarnings("resource")
  @Override
  public File generateReport(User user, File fp, Sale sale) throws ApplicationException {
    Document document = null;

    log.debug("generateReport(): generating sale report.. ");
    try {
      final Map<String, Configuration> configs = configService.getUserConfiguration(user.getId());
      final boolean
          isProductCodeIncluded =
          configs.get(ConfigurationKey.SALE_INFO_PROD_CODE.getCode()).getValue().equals("1");
      final boolean
          isProductDescriptionIncluded =
          configs.get(ConfigurationKey.SALE_INFO_PROD_DESCRIPTION.getCode()).getValue().equals("1");
      final boolean
          isProductQtyIncluded =
          configs.get(ConfigurationKey.SALE_INFO_PROD_QTY.getCode()).getValue().equals("1");
      final boolean
          isProductPriceIncluded =
          configs.get(ConfigurationKey.SALE_INFO_PROD_PRICE.getCode()).getValue().equals("1");
      final boolean
          isDiscountIncluded =
          configs.get(ConfigurationKey.SALE_INFO_DISCOUNT.getCode()).getValue().equals("1");
      final boolean
          isVATIncluded =
          configs.get(ConfigurationKey.SALE_INFO_VAT.getCode()).getValue().equals("1");
      final boolean
          isTotalsIncluded =
          configs.get(ConfigurationKey.SALE_INFO_TOTALS.getCode()).getValue().equals("1");

      // prepare the fonts
      final Font headerFont = new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.NORMAL);
      final Font regularFont = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.NORMAL);
      final Font
          sectionFont =
          new Font(Font.FontFamily.TIMES_ROMAN, 11, Font.BOLD | Font.UNDERLINE);
      final Font boldFont = new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD);
      final Font
          tableHeaderFont =
          new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD, BaseColor.WHITE);

      // create the document
      document = new Document(PageSize.A4, 30, 30, 30, 30);

      if (!fp.getParentFile().exists()) {
        fp.getParentFile().mkdirs();
      }
      PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(fp));
      writer.setBoxSize("art", new Rectangle(36, 54, 559, 788));

      // sets the footer
      writer.setPageEvent(new Footer());

      // opens the document for writing
      document.open();

      // sets the metadata
      document.addTitle("BM - Resumo da venda " + sale.getId());
      document.addLanguage("PT");
      document.addAuthor("BM (c) David Silva");
      document.addCreator("BM (c) David Silva");

      // sets the header
      Paragraph header = new Paragraph();
      header.add(
          new Paragraph("Frutogal - Ind\u00FAstria e Com\u00E9rcio de Produtos alimentares, Lda.",
              headerFont));
      header.add(new Paragraph("Rua da Junqueira, 200 Porta 14", headerFont));
      header.add(new Paragraph("1300-346 Lisboa", headerFont));
      header.add(new Paragraph("NIF: 501509100", headerFont));
      document.add(header);

      addEmptyLines(document, 2);

      // sets the basic client information
      Client client = sale.getClient();
      Paragraph clientParagraph = new Paragraph();
      clientParagraph.add(
          new Paragraph("Informa\u00E7\u00E3o do cliente " + client.getCode() + ":", sectionFont));
      addFormItem(clientParagraph, boldFont, "Nome: ", regularFont, client.getName());
      addFormItem(clientParagraph, boldFont, "Morada: ", regularFont, client.getAddress());
      addFormItem(clientParagraph, boldFont, "NIF: ", regularFont, client.getVatNumber());
      if (client.getPhone() != null && !client.getPhone().trim().isEmpty()) {
        addFormItem(clientParagraph, boldFont, "Telefone: ", regularFont, client.getPhone());
      }
      if (client.getFax() != null && !client.getFax().trim().isEmpty()) {
        addFormItem(clientParagraph, boldFont, "Fax: ", regularFont, client.getFax());
      }
      if (sale.hasCardex()) {
        clientParagraph.add(new Chunk("Cardex: ", boldFont));
        try {
          clientParagraph.add(
              new Chunk(Image.getInstance(SaleService.class.getResource("/assets/check.png")), 0f,
                  0f));
        } catch (IOException e) {
          clientParagraph.add(new Chunk("Sim", regularFont));
        }
        clientParagraph.add(Chunk.NEWLINE);
      }
      document.add(clientParagraph);

      addEmptyLines(document, 2);

      // sale header
      Paragraph saleHeaderParagraph = new Paragraph();
      saleHeaderParagraph.setAlignment(Element.ALIGN_CENTER);
      saleHeaderParagraph.add(new Paragraph("Venda",
          new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD | Font.UNDERLINE)));
      saleHeaderParagraph.add(Chunk.NEWLINE);
      document.add(saleHeaderParagraph);

      // sets the header contents
      PdfPTable saleHeaderTable = new PdfPTable(5);
      saleHeaderTable.setWidthPercentage(100);
      saleHeaderTable.setHeaderRows(1);

      PdfPCell cell = new PdfPCell(new Phrase("Local descarga", tableHeaderFont));
      cell.setHorizontalAlignment(Element.ALIGN_CENTER);
      cell.setBackgroundColor(BaseColor.GRAY);
      saleHeaderTable.addCell(cell);

      //cell = new PdfPCell(new Phrase("N\u00BA venda", tableHeaderFont));
      cell = new PdfPCell(new Phrase("N\u00BA vendedor", tableHeaderFont));
      cell.setHorizontalAlignment(Element.ALIGN_CENTER);
      cell.setBackgroundColor(BaseColor.GRAY);
      saleHeaderTable.addCell(cell);

      cell = new PdfPCell(new Phrase("Condi\u00E7\u00F5es pagamento", tableHeaderFont));
      cell.setHorizontalAlignment(Element.ALIGN_CENTER);
      cell.setBackgroundColor(BaseColor.GRAY);
      saleHeaderTable.addCell(cell);

      cell = new PdfPCell(new Phrase("Observa\u00E7\u00F5es", tableHeaderFont));
      cell.setHorizontalAlignment(Element.ALIGN_CENTER);
      cell.setBackgroundColor(BaseColor.GRAY);
      saleHeaderTable.addCell(cell);

      cell = new PdfPCell(new Phrase("Data doc.", tableHeaderFont));
      cell.setHorizontalAlignment(Element.ALIGN_CENTER);
      cell.setBackgroundColor(BaseColor.GRAY);
      saleHeaderTable.addCell(cell);

      float[] saleHeaderTableColumnWidths = new float[]{25f, 10f, 25f, 25f, 15f};
      saleHeaderTable.setWidths(saleHeaderTableColumnWidths);

      saleHeaderTable.addCell(new Phrase(sale.getDischargePlace(), regularFont));
      //cell = new PdfPCell(new Phrase(String.valueOf(sale.getId()), regularFont));
      cell = new PdfPCell(new Phrase("18", regularFont));
      cell.setHorizontalAlignment(Element.ALIGN_CENTER);
      saleHeaderTable.addCell(cell);
      saleHeaderTable.addCell(new Phrase(sale.getPaymentConditions(), regularFont));
      saleHeaderTable.addCell(new Phrase(sale.getObservations(), regularFont));
      saleHeaderTable.addCell(
          new Phrase(DateFormat.getDateInstance().format(sale.getSaleDate()), regularFont));

      document.add(saleHeaderTable);
      addEmptyLines(document, 2);

      // sets the products
      int totalColumns = 0 +
          (isProductCodeIncluded ? 1 : 0) +
          (isProductDescriptionIncluded ? 1 : 0) +
          (isProductQtyIncluded ? 1 : 0) +
          (isProductPriceIncluded ? 1 : 0) +
          (isDiscountIncluded ? 1 : 0) +
          (isVATIncluded ? 1 : 0) +
          (isTotalsIncluded ? 1 : 0);

      PdfPTable saleTable = new PdfPTable(totalColumns);
      saleTable.setWidthPercentage(100);
      saleTable.setHeaderRows(1);

      if (isProductCodeIncluded) {
        cell = new PdfPCell(new Phrase("C\u00F3digo", tableHeaderFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBackgroundColor(BaseColor.GRAY);
        saleTable.addCell(cell);
      }

      if (isProductDescriptionIncluded) {
        cell = new PdfPCell(new Phrase("Designa\u00E7\u00E3o", tableHeaderFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBackgroundColor(BaseColor.GRAY);
        saleTable.addCell(cell);
      }

      if (isProductQtyIncluded) {
        cell = new PdfPCell(new Phrase("Quant.", tableHeaderFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBackgroundColor(BaseColor.GRAY);
        saleTable.addCell(cell);
      }

      if (isProductPriceIncluded) {
        cell = new PdfPCell(new Phrase("Pre\u00E7o unit.", tableHeaderFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBackgroundColor(BaseColor.GRAY);
        saleTable.addCell(cell);
      }

      if (isDiscountIncluded) {
        cell = new PdfPCell(new Phrase("% Desc", tableHeaderFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBackgroundColor(BaseColor.GRAY);
        saleTable.addCell(cell);
      }

      if (isVATIncluded) {
        cell = new PdfPCell(new Phrase("% IVA", tableHeaderFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBackgroundColor(BaseColor.GRAY);
        saleTable.addCell(cell);
      }

      if (isTotalsIncluded) {
        cell = new PdfPCell(new Phrase("Total", tableHeaderFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBackgroundColor(BaseColor.GRAY);
        saleTable.addCell(cell);
      }

      float[] saleTableColumnWidths = new float[totalColumns];
      int idx = 0;
      if (isProductCodeIncluded) {
        saleTableColumnWidths[idx++] = 10f;
      }
      if (isProductDescriptionIncluded) {
        saleTableColumnWidths[idx++] = 30f;
      }
      if (isProductQtyIncluded) {
        saleTableColumnWidths[idx++] = 10f;
      }
      if (isProductPriceIncluded) {
        saleTableColumnWidths[idx++] = 10f;
      }
      if (isDiscountIncluded) {
        saleTableColumnWidths[idx++] = 10f;
      }
      if (isVATIncluded) {
        saleTableColumnWidths[idx++] = 10f;
      }
      if (isTotalsIncluded) {
        saleTableColumnWidths[idx++] = 20f;
      }
      saleTable.setWidths(saleTableColumnWidths);

      // iterate over the products
      Product baseProduct = null;
      for (SaleProduct product : sale.getProducts()) {
        baseProduct = product.getBaseProduct();
        if (isProductCodeIncluded) {
          saleTable.addCell(new Phrase(baseProduct.getCode(), regularFont));
        }

        if (isProductDescriptionIncluded) {
          saleTable.addCell(new Phrase(baseProduct.getDescription(), regularFont));
        }

        if (isProductQtyIncluded) {
          String unit = product.getUnit() == null ? "" : "" + product.getUnit().getLiteral();
          if (product.getAmount() % 1.0 == 0) {
            cell =
                new PdfPCell(new Phrase(String.format("%d%s", (int) product.getAmount(), unit),
                    regularFont));
          } else {
            cell =
                new PdfPCell(
                    new Phrase(String.format("%.2f%s", product.getAmount(), unit), regularFont));
          }
          cell.setHorizontalAlignment(Element.ALIGN_CENTER);
          saleTable.addCell(cell);
        }

        if (isProductPriceIncluded) {
          if (product.getPrice() == null || product.getPrice() == 0) {
            saleTable.addCell(new Phrase("", regularFont));
          } else {
            if (product.getPrice() % 1.0 == 0) {
              saleTable.addCell(
                  new Phrase(String.format("%d \u20ac", (int) product.getPrice().doubleValue()),
                      regularFont));
            } else {
              saleTable.addCell(
                  new Phrase(String.format("%.2f \u20ac", product.getPrice()), regularFont));

            }
          }
        }

        if (isDiscountIncluded) {
          if (product.getDiscount() == null || product.getDiscount().trim().isEmpty()) {
            saleTable.addCell(new Phrase("", regularFont));
          } else {
            try {
              Calculable calc = new ExpressionBuilder(product.getDiscount().trim()).build();
              double discount = calc.calculate();
              if (isDouble(product.getDiscount().trim()) ||
                  isInteger(product.getDiscount().trim())) {
                if (discount % 1.0 == 0) {
                  cell = new PdfPCell(new Phrase(String.format("%d", (int) discount), regularFont));
                } else {
                  cell = new PdfPCell(new Phrase(String.format("%.2f", discount), regularFont));
                }
              } else {
                cell = new PdfPCell(new Phrase(product.getDiscount().trim(), regularFont));
              }
              cell.setHorizontalAlignment(Element.ALIGN_CENTER);
              saleTable.addCell(cell);
            } catch (Exception e) {
              saleTable.addCell(new Phrase("", regularFont));
            }
          }
        }

        if (isVATIncluded) {
          if (product.getVat() == null) {
            saleTable.addCell(new Phrase("", regularFont));
          } else {
            if (product.getVat() % 1.0 == 0) {
              cell =
                  new PdfPCell(new Phrase(String.format("%d", (int) product.getVat().doubleValue()),
                      regularFont));
            } else {
              cell = new PdfPCell(new Phrase(String.format("%2.f", product.getVat()), regularFont));
            }
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            saleTable.addCell(cell);
          }
        }

        if (isTotalsIncluded) {
          if (product.getPrice() == null) {
            saleTable.addCell(new Phrase("0 \u20ac", regularFont));
          } else {
            double totals = product.getPrice() * product.getAmount();
            if (totals % 1.0 == 0) {
              saleTable.addCell(new Phrase(String.format("%d \u20ac", (int) totals), regularFont));
            } else {
              saleTable.addCell(new Phrase(String.format("%.2f \u20ac", totals), regularFont));
            }
          }
        }
      }

      document.add(saleTable);

      // sets the totals
      if (isTotalsIncluded) {
        PdfPTable totalsTable = new PdfPTable(2);
        totalsTable.setWidthPercentage(100);
        totalsTable.setHeaderRows(0);

        float[] totalsTableColumnWidths = new float[]{80f, 20f};
        totalsTable.setWidths(totalsTableColumnWidths);

        cell = new PdfPCell(new Phrase("Total bruto", tableHeaderFont));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        totalsTable.addCell(cell);
        totalsTable
            .addCell(new Phrase(String.format("%.2f \u20ac", sale.getTotalBrute()), regularFont));

        cell = new PdfPCell(new Phrase("Total IVA", tableHeaderFont));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        totalsTable.addCell(cell);
        totalsTable
            .addCell(new Phrase(String.format("%.2f \u20ac", sale.getTotalVat()), regularFont));

        cell = new PdfPCell(new Phrase("Total desconto", tableHeaderFont));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        totalsTable.addCell(cell);
        totalsTable.addCell(
            new Phrase(String.format("%.2f \u20ac", sale.getTotalDiscount()), regularFont));

        cell = new PdfPCell(new Phrase("Total liquido", tableHeaderFont));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        totalsTable.addCell(cell);
        totalsTable
            .addCell(new Phrase(String.format("%.2f \u20ac", sale.getTotalLiquid()), regularFont));

        document.add(totalsTable);
      }
      cell = null;

      log.debug("generateReport(): successfully generated sale report.");
    } catch (FileNotFoundException e) {
      log.error("generateReport(): error generating sale report", e);
      e.printStackTrace();
      throw new ApplicationException("Ocorreu um erro ao gerar o resumo da venda.");
    } catch (DocumentException e) {
      log.error("generateReport(): error generating sale report", e);
      e.printStackTrace();
      throw new ApplicationException("Ocorreu um erro ao gerar o resumo da venda.");
    } catch (RuntimeException e) {
      log.error("generateReport(): error generating sale report", e);
      e.printStackTrace();
      throw new ApplicationException("Ocorreu um erro ao gerar o resumo da venda.");
    } finally {
      // close the document
      if (document != null) {
        try {
          document.close();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }

    return fp;
  }

  /**
   * Adds the given amount of empty lines the the document
   *
   * @param doc   The document to be modified
   * @param lines The number of empty lines
   * @throws DocumentException
   */
  private void addEmptyLines(Document doc, int lines) throws DocumentException {
    for (int i = 0; i < lines; i++) {
      doc.add(new Paragraph(" "));
    }
  }

  /**
   * Creates and adds a new form to the given paragraph
   *
   * @param paragraph The paragraph to be modified
   * @param labelFont The label font
   * @param label     The label text
   * @param valueFont The value font
   * @param value     The value text
   */
  private void addFormItem(Paragraph paragraph, Font labelFont, String label, Font valueFont,
      String value) {
    paragraph.add(new Chunk(label == null ? "" : label, labelFont));
    paragraph.add(new Chunk(value == null ? "" : value, valueFont));
    paragraph.add(Chunk.NEWLINE);
  }

  /**
   * Inner class to add a footer.
   *
   * @author David Silva <david@davidafsilva.pt>
   * @date 10:25:48 AM
   */
  static class Footer extends PdfPageEventHelper {

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
      // set the footer fonts
      final Font pageFont = new Font(Font.FontFamily.TIMES_ROMAN, 8, Font.NORMAL);
      final Font footerFont = new Font(Font.FontFamily.TIMES_ROMAN, 6, Font.NORMAL);
      final Font
          copyrightsFont =
          new Font(Font.FontFamily.TIMES_ROMAN, 6, Font.NORMAL, BaseColor.GRAY);

      Rectangle rect = writer.getBoxSize("art");
      // sets the warning
      ColumnText.showTextAligned(writer.getDirectContent(),
          Element.ALIGN_LEFT, new Phrase("Este documento n\u00E3o serve de factura.", footerFont),
          rect.getLeft(), rect.getBottom() - 28, 0);
      // sets the page counter
      ColumnText.showTextAligned(writer.getDirectContent(),
          Element.ALIGN_CENTER,
          new Phrase(String.format("p\u00E1gina %d", writer.getPageNumber()), pageFont),
          (rect.getLeft() + rect.getRight()) / 2, rect.getBottom() - 18, 0);
      // sets the copyrights
      ColumnText.showTextAligned(writer.getDirectContent(),
          Element.ALIGN_RIGHT, new Phrase("BM 2013 (c) David Silva", copyrightsFont),
          rect.getRight(), rect.getBottom() - 28, 0);
    }
  }

  /* (non-Javadoc)
   * @see pt.davidafsilva.bm.server.service.interfaces.ISaleService#sendReport(java.io.File)
   */
  @Override
  public void sendReport(User user, Sale sale, File fp) throws ApplicationException {
    log.debug("sendReport(): sending report via e-mail..");
    try {
      Map<String, Configuration> configs = configService.getUserConfiguration(user.getId());

      String author = configs.get(ConfigurationKey.EMAIL_AUTHOR.getCode()).getValue();
      if (author == null || author.trim().isEmpty()) {
        author = "Business Manager";
      }
      String authorEmail = configs.get(ConfigurationKey.EMAIL_ADDRESS_SOURCE.getCode()).getValue();
      if (authorEmail == null || authorEmail.trim().isEmpty()) {
        throw new IllegalStateException("invalid source e-mail address");
      }
      author += " <" + authorEmail + ">";
      // FIXME: change this
      final String authorPass = configs.get(ConfigurationKey.EMAIL_ADDRESS_SOURCE_PWD.getCode())
          .getValue();

      // subject
      final StringBuilder subject = new StringBuilder(configs.get(ConfigurationKey.EMAIL_SUBJECT
          .getCode()).getValue());
      replaceKeywords(subject, sale);

      // message
      final StringBuilder message = new StringBuilder(configs.get(ConfigurationKey.EMAIL_BODY
          .getCode()).getValue());
      replaceKeywords(message, sale);

      sendEmail(authorEmail, authorPass, author,
          configs.get(ConfigurationKey.EMAIL_ADDRESS_TARGET.getCode()).getValue(), null, null,
          subject.toString(), message.toString(),
          fp, EmailProcotol.TLS);

      markSaleHasSentByEmail(sale);

      log.debug("sendReport(): successfully sent the report via e-mail.");
    } catch (RuntimeException e) {
      log.debug("sendReport(): error sending the report via e-mail", e);
      throw new ApplicationException("Ocorreu um erro a enviar o resumo da venda por e-mail.");
    }
  }

  private void replaceKeywords(final StringBuilder sb, final Sale sale) {
    int idx = sb.indexOf("%CLIENTE%");
    if (idx > 0) {
      sb.replace(idx, idx + "%CLIENTE%".length(), sale.getClient().getName());
    }
  }

  /**
   * The protocol to use for the communication when sending emails
   *
   * @author David Silva <david@davidafsilva.pt>
   * @date 10:31:48 PM
   */
  private enum EmailProcotol {
    TLS, SSL
  }

  /**
   * Sends an email to a given destinations
   *
   * @param from     The from address source of the email
   *                 The from will be ignored by the SMTP server if it's configured to
   *                 use the username (email) as source of the form parameter
   * @param to       The destiny addresses of the email (comma separated for multiple addresses)
   * @param cc       The CC addresses (comma separated for multiple addresses)
   * @param bcc      The BCC addresses (comma separated for multiple addresses)
   * @param subject  The email subject
   * @param body     The email body (MIME)
   * @param protocol The communication protocol to be used
   * @throws RuntimeException if any exception is thrown when sending the email
   */
  private static void sendEmail(String username, String password,
      String from,
      String to, String cc, String bcc,
      String subject, String body,
      File attachment,
      EmailProcotol protocol) throws RuntimeException {

    Properties props = new Properties();
    switch (protocol) {
      case TLS:
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        break;
      case SSL:
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        break;
    }

    Session session = Session.getInstance(props, new Authenticator() {

      /* (non-Javadoc)
       * @see javax.mail.Authenticator#getPasswordAuthentication()
       */
      @Override
      protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(username, password);
      }
    });

    try {
      Message message = new MimeMessage(session);
      message.setSentDate(new Date());
      message.setFrom(new InternetAddress(from));
      message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
      if (cc != null) {
        message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(cc));
      }
      if (bcc != null) {
        message.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(bcc));
      }
      message.setSubject(subject);

      // create and fill the first message part
      MimeBodyPart mbp1 = new MimeBodyPart();
      mbp1.setText(body);

      // create the second message part
      MimeBodyPart mbp2 = new MimeBodyPart();

      // attach the file to the message
      FileDataSource fds = new FileDataSource(attachment);
      mbp2.setDataHandler(new DataHandler(fds));
      mbp2.setFileName(fds.getName());

      // create the Multipart and add its parts to it
      Multipart mp = new MimeMultipart();
      mp.addBodyPart(mbp1);
      mp.addBodyPart(mbp2);

      // add the Multipart to the message
      message.setContent(mp);

      Transport.send(message);

    } catch (MessagingException e) {
      throw new RuntimeException(e);
    }
  }

  private boolean isDouble(String txt) {
    try {
      Double.parseDouble(txt);
      return true;
    } catch (NumberFormatException nfe) {
      return false;
    }
  }

  private boolean isInteger(String txt) {
    try {
      Integer.parseInt(txt);
      return true;
    } catch (NumberFormatException nfe) {
      return false;
    }
  }
}
