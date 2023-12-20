import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

// İşlem türünü temsil eden enum
enum İşlemTürü {
    AL, SAT
}

// Bir ürünün özelliklerini ve metotlarını tanımlayan Product arayüzü
interface Ürün {
    String getAd();
    double getFiyat();
    int getAdet();
    String getKategori();
    void setAdet(int adet);
    void görüntüle();
    void ürünBilgisiniGüncelle(String ad, double fiyat, int adet, String kategori);
}

// Soyut sınıf, Product arayüzünü uygular ve alt sınıflar için ortak alanlar ve metotlar sağlar
abstract class SoyutÜrün implements Ürün {
    protected String ad;
    protected double fiyat;
    protected int adet;
    protected String kategori;

    public SoyutÜrün(String ad, double fiyat, int adet, String kategori) {
        this.ad = ad;
        this.fiyat = fiyat;
        this.adet = adet;
        this.kategori = kategori;
    }

    @Override
    public void ürünBilgisiniGüncelle(String ad, double fiyat, int adet, String kategori) {
        this.ad = ad;
        this.fiyat = fiyat;
        this.adet = adet;
        this.kategori = kategori;
    }

    @Override
    public String getAd() {
        return ad;
    }

    @Override
    public double getFiyat() {
        return fiyat;
    }

    @Override
    public int getAdet() {
        return adet;
    }

    @Override
    public String getKategori() {
        return kategori;
    }

    @Override
    public void setAdet(int adet) {
        this.adet = adet;
    }

    @Override
    public void görüntüle() {
        System.out.println("Ad: " + ad + ", Fiyat: " + fiyat + ", Adet: " + adet + ", Kategori: " + kategori);
    }
}

// SoyutÜrün sınıfının alt sınıfı, bir kitabı temsil eder
class Kitap extends SoyutÜrün {
    private String yazar;
    private int sayfaSayısı;

    public Kitap(String ad, double fiyat, int adet, String kategori, String yazar, int sayfaSayısı) {
        super(ad, fiyat, adet, kategori);
        this.yazar = yazar;
        this.sayfaSayısı = sayfaSayısı;
    }

    public String getYazar() {
        return yazar;
    }

    public int getSayfaSayısı() {
        return sayfaSayısı;
    }

    @Override
    public void görüntüle() {
        super.görüntüle();
        System.out.println("Yazar: " + yazar + ", Sayfa Sayısı: " + sayfaSayısı);
    }
}

// SoyutÜrün sınıfının alt sınıfı, bir gıda ürününü temsil eder
class Gıda extends SoyutÜrün {
    private String sonKullanmaTarihi;
    private boolean vejetaryen;

    public Gıda(String ad, double fiyat, int adet, String kategori, String sonKullanmaTarihi, boolean vejetaryen) {
        super(ad, fiyat, adet, kategori);
        this.sonKullanmaTarihi = sonKullanmaTarihi;
        this.vejetaryen = vejetaryen;
    }

    public String getSonKullanmaTarihi() {
        return sonKullanmaTarihi;
    }

    public boolean isVejetaryen() {
        return vejetaryen;
    }

    @Override
    public void görüntüle() {
        super.görüntüle();
        System.out.println("Son Kullanma Tarihi: " + sonKullanmaTarihi + ", Vejetaryen: " + vejetaryen);
    }
}

// Generic class that implements ProductManagement interface and uses a generic collection (List) to store and manage products
class Depo<T extends Ürün> implements ÜrünYönetimi<T> {
    private List<T> ürünler;

    public Depo() {
        ürünler = new ArrayList<>();
    }

    @Override
    public void ürünEkle(T ürün) {
        ürünler.add(ürün);
        System.out.println(ürün.getAd() + " depoya eklendi.");
    }

    @Override
    public void ürünSil(String ürünAdı) {
        ürünler.removeIf(ürün -> ürün.getAd().equals(ürünAdı));
        System.out.println(ürünAdı + " depodan silindi.");
    }

    @Override
    public void ürünleriGörüntüle() {
        System.out.println("Mevcut Ürünler:");
        ürünler.forEach(ürün -> ürün.görüntüle());
    }

    @Override
    public void ürünAl(String ürünAdı, int adet) {
        T ürün = ürünler.stream()
                .filter(p -> p.getAd().equals(ürünAdı))
                .findFirst()
                .orElse(null);

        if (ürün != null) {
            ürün.setAdet(ürün.getAdet() + adet);
            System.out.println(ürünAdı + " ürününden " + adet + " adet satın alındı.");
            İşlem.işlemiGerçekleştir(ürün, adet, true);
        } else {
            System.out.println("Ürün bulunamadı!");
        }
    }

    @Override
    public void ürünSat(String ürünAdı, int adet) {
        T ürün = ürünler.stream()
                .filter(p -> p.getAd().equals(ürünAdı))
                .findFirst()
                .orElse(null);

        if (ürün != null) {
            if (ürün.getAdet() >= adet) {
                ürün.setAdet(ürün.getAdet() - adet);
                System.out.println(ürünAdı + " ürününden " + adet + " adet satıldı.");
                İşlem.işlemiGerçekleştir(ürün, adet, false);
            } else {
                System.out.println("Yeterli stok yok!");
            }
        } else {
            System.out.println("Ürün bulunamadı!");
        }
    }

    @Override
    public void fiyataGöreSırala() {
        // Using a lambda function to compare two products by their prices
        ürünler.sort(Comparator.comparingDouble(Ürün::getFiyat));
        System.out.println("Ürünler fiyata göre sıralandı.");
    }

    @Override
    public void ismeGöreSırala() {
        // Using a lambda function to compare two products by their names
        ürünler.sort(Comparator.comparing(Ürün::getAd));
        System.out.println("Ürünler isme göre sıralandı.");
    }

    @Override
    public void kategoriyeGöreSırala() {
        // Using a lambda function to compare two products by their categories
        ürünler.sort(Comparator.comparing(Ürün::getKategori));
        System.out.println("Ürünler kategoriye göre sıralandı.");
    }

    @Override
    public void kategoriyeGöreFiltrele(String kategori) {
        // Using a lambda function to filter products by their categories
        List<T> filtrelenmişÜrünler = ürünler.stream()
                .filter(p -> p.getKategori().equals(kategori))
                .collect(Collectors.toList());
        System.out.println(kategori + " kategorisine göre filtrelenmiş ürünler:");
        filtrelenmişÜrünler.forEach(ürün -> ürün.görüntüle());
    }

    @Override
    public T getÜrünAdıyla(String ürünAdı) {
        return ürünler.stream()
                .filter(p -> p.getAd().equals(ürünAdı))
                .findFirst()
                .orElse(null);
    }
}

// Ürünleri yönetmek için arayüz
interface ÜrünYönetimi<T extends Ürün> {
    void ürünEkle(T ürün);
    void ürünSil(String ürünAdı);
    void ürünleriGörüntüle();
    void ürünAl(String ürünAdı, int adet);
    void ürünSat(String ürünAdı, int adet);
    void fiyataGöreSırala();
    void ismeGöreSırala();
    void kategoriyeGöreSırala();
    void kategoriyeGöreFiltrele(String kategori);
    T getÜrünAdıyla(String ürünAdı);
}

// Bir işlemi (alım veya satım) temsil eden sınıf
class İşlem {
    public static <T extends Ürün> void işlemiGerçekleştir(T ürün, int adet, boolean alınıyor) {
        double toplamMaliyet = ürün.getFiyat() * adet;
        String işlemTürü = alınıyor ? "Alındı" : "Satıldı";
        System.out.println(adet + " adet " + ürün.getAd() + " ürünü " + (alınıyor ? "maliyeti" : "geliri") + " toplam $" + toplamMaliyet + " karşılığında " + işlemTürü);
    }
}
// Depo yönetim sisteminin işlevselliğini göstermek için bir test sınıfı
public class Test {
    public static void main(String[] args) {
        // Bir depo nesnesi oluşturuluyor
        Depo<Ürün> depo = new Depo<>();

        Scanner tarayıcı = new Scanner(System.in);

        // Bazı ürün nesneleri oluşturuluyor
        Kitap kitap1 = new Kitap("Suç ve Ceza", 10.0, 100, "Kitaplar", " Fyodor Dostoyevski", 200);
        Kitap kitap2 = new Kitap("Sefiller", 15.0, 50, "Kitaplar", "Victor Hugo", 300);

        // Ürünler depoya ekleniyor
        depo.ürünEkle(kitap1);
        depo.ürünEkle(kitap2);

        // Mevcut ürünleri görüntüleme
        depo.ürünleriGörüntüle();

        // Ürünleri satın alma ve satma
        System.out.print("Satın almak istediğiniz ürünün adını girin: ");
        String satınAlınacakÜrün = tarayıcı.nextLine();
        System.out.print("Satın alınacak miktarı girin: ");
        int satınAlmaMiktarı = tarayıcı.nextInt();
        tarayıcı.nextLine(); // Yeni satır karakterini tüket
        depo.ürünAl(satınAlınacakÜrün, satınAlmaMiktarı);

        System.out.print("Satmak istediğiniz ürünün adını girin: ");
        String satılacakÜrün = tarayıcı.nextLine();
        System.out.print("Satılacak miktarı girin: ");
        int satışMiktarı = tarayıcı.nextInt();
        tarayıcı.nextLine(); // Yeni satır karakterini tüket
        depo.ürünSat(satılacakÜrün, satışMiktarı);

        // Ürünleri sıralama ve filtreleme
        depo.ismeGöreSırala();
        System.out.print("Filtrelemek için kategoriyi girin: ");
        String kategoriFiltrele = tarayıcı.nextLine();
        depo.kategoriyeGöreFiltrele(kategoriFiltrele);

        // Güncellenmiş ürün bilgileri
        System.out.print("Güncellenecek ürünün adını girin: ");
        String güncellenecekÜrün = tarayıcı.nextLine();
        System.out.print("Yeni adı girin: ");
        String yeniAd = tarayıcı.nextLine();
        System.out.print("Yeni fiyatı girin: ");
        double yeniFiyat = tarayıcı.nextDouble();
        System.out.print("Yeni miktarı girin: ");
        int yeniAdet = tarayıcı.nextInt();
        tarayıcı.nextLine(); // Yeni satır karakterini tüket
        System.out.print("Yeni kategoriyi girin: ");
        String yeniKategori = tarayıcı.nextLine();

        Ürün güncellenecekÜrünObjesi = depo.getÜrünAdıyla(güncellenecekÜrün);
        if (güncellenecekÜrünObjesi != null) {
            güncellenecekÜrünObjesi.ürünBilgisiniGüncelle(yeniAd, yeniFiyat, yeniAdet, yeniKategori);
            System.out.println("Ürün bilgileri güncellendi.");
        } else {
            System.out.println("Ürün bulunamadı!");
        }

        // Güncellenmiş ürünleri görüntüleme
        depo.ürünleriGörüntüle();
    }
}