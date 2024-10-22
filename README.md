LATIHAN UTS 
--------------------------------------------
Fitur #1. Melihat Daftar Ruangan 

Model (RoomModel) Pastikan kamu sudah memiliki RoomModel yang mendefinisikan entitas ruangan seperti ini:

@Table(name = "room")
public class RoomModel implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long roomId;

    @Column(name = "room_number")
    private String roomNumber;

    @Column(name = "room_name")
    private String roomName;

    @Column(name = "building_name")
    private String buildingName;

    @Column(name = "faculty")
    private String faculty;

    // Getter dan Setter
}

2. Repository (RoomRepository) Buat repository untuk berinteraksi dengan database:

public interface RoomRepository extends JpaRepository<RoomModel, Long> {
}

3. Service (RoomService) Service untuk menangani logika bisnis:
@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;

    public List<RoomModel> getAllRooms() {
        return roomRepository.findAll();
    }
}

4. Controller (RoomController) Buat controller untuk menangani request dari URL:

java
Salin kode
@Controller
public class RoomController {

    @Autowired
    private RoomService roomService;

    @GetMapping("/ruang/view-all")
    public String viewAllRooms(Model model) {
        List<RoomModel> rooms = roomService.getAllRooms();
        model.addAttribute("rooms", rooms);
        return "room/view-all";
    }
}

5. <!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Daftar Ruangan</title>
</head>
<body>
    <h1>Daftar Ruangan</h1>
    <table border="1">
        <thead>
            <tr>
                <th>No</th>
                <th>Nama Ruangan</th>
                <th>Nomor Ruangan</th>
                <th>Gedung</th>
                <th>Fakultas</th>
                <th>Action</th>
            </tr>
        </thead>
        <tbody>
            <tr th:each="room, iter : ${rooms}">
                <td th:text="${iter.count}"></td>
                <td th:text="${room.roomName}"></td>
                <td th:text="${room.roomNumber}"></td>
                <td th:text="${room.buildingName}"></td>
                <td th:text="${room.faculty}"></td>
                <td>
                    <a th:href="@{/ruang/detail/{id}(id=${room.roomId})}">Detail</a>
                </td>
            </tr>
        </tbody>
    </table>
</body>
</html>

6. url mapping: http://localhost:8080/ruang/view-all

----------------------------------------------

Fitur #2. Menambah data Ruangan baru 

1. Buat form di thymeleaf: room/form-add
<!DOCTYPE html>
<html lang="en" xmlns:th="http://thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>Tambah Ruangan</title>
    <object th:insert="~{fragments/fragment::css}" th:remove="tag"></object>
    <object th:insert="~{fragments/fragment::js}" th:remove="tag"></object>
</head>
<body>
<nav th:replace="~{fragments/fragment::navbar(~{::home})}"></nav>

<div class="container">
    <h1>Tambah Ruangan</h1>

    <form action="#" th:action="@{/ruang/add}" th:object="${room}" method="post">
        <div class="form-group">
            <label for="roomNumber">Nomor/Kode Ruangan</label>
            <input type="text" class="form-control" id="roomNumber" th:field="*{roomNumber}" placeholder="Masukkan nomor/kode ruangan" required>
        </div>

        <div class="form-group">
            <label for="roomName">Nama Ruangan</label>
            <input type="text" class="form-control" id="roomName" th:field="*{roomName}" placeholder="Masukkan nama ruangan" required>
        </div>

        <div class="form-group">
            <label for="buildingName">Nama Gedung</label>
            <input type="text" class="form-control" id="buildingName" th:field="*{buildingName}" placeholder="Masukkan nama gedung" required>
        </div>

        <div class="form-group">
            <label for="faculty">Fakultas</label>
            <select class="form-control" id="faculty" th:field="*{faculty}">
                <option value="FASILKOM">FASILKOM</option>
                <option value="FK">FK</option>
                <option value="FT">FT</option>
                <option value="FISIP">FISIP</option>
                <option value="FIB">FIB</option>
            </select>
        </div>

        <button type="submit" class="btn btn-primary">Submit</button>
        <a href="#" th:href="@{/ruang/view-all}" class="btn btn-secondary">Back</a>
    </form>
</div>
</body>
</html>

2. Bikin endpoint buat handle form submission 
@Controller
public class RoomController {

    @Autowired
    private RoomService roomService;

    // Menampilkan form tambah ruangan
    @GetMapping("/ruang/add")
    public String showAddRoomForm(Model model) {
        model.addAttribute("room", new RoomModel());
        return "room/add-room";
    }

    // Menangani submit form untuk menambah ruangan
    @PostMapping("/ruang/add")
    public String addRoom(@ModelAttribute RoomModel room, RedirectAttributes redirectAttributes) {
        roomService.saveRoom(room);
        redirectAttributes.addFlashAttribute("success", "Ruangan berhasil disimpan dengan id " + room.getRoomId());
        return "redirect:/ruang/view-all";
    }
}

3. Room service buat nyimpen ruangan baru
@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;

    public void saveRoom(RoomModel room) {
        roomRepository.save(room);
    }

    public List<RoomModel> getAllRooms() {
        return roomRepository.findAll();
    }
}

4. RoomRepository
public interface RoomRepository extends JpaRepository<RoomModel, Long> {
}

5. Di bagian controller, gunakan redirectattributes, dan jg halaman dialihkan ke "view-all".
redirectAttributes.addFlashAttribute("success", "Ruangan berhasil disimpan dengan id " + room.getRoomId());

6. Pada halaman view-all.html, tambahkan kode ini untuk menampilkan pesan berhasil:
<div class="alert alert-success" th:text="${success}" th:if="${success}"></div>
URL = http://localhost:8080/ruang/add

---------------------------------------------

Fitur #3 Menambahkan masalah barang rusak per ruangan:
1. Menambahkan issue model:
@Entity
@Table(name = "issue")
public class IssueModel implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "issue_id")
    private Long issueId;

    @Column(name = "description")
    private String description;

    @Column(name = "reported_date")
    private LocalDate reportedDate;

    @Column(name = "reporter")
    private String reporter;

    @Column(name = "status")
    private String status = "new";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", referencedColumnName = "room_id")
    private RoomModel room;

    // Getter dan Setter
}

2. Buat repository untuk mengelola data issue:
public interface IssueRepository extends JpaRepository<IssueModel, Long> {
}

3. Service untuk menyimpan data masalah barang rusak:
@Service
public class IssueService {

    @Autowired
    private IssueRepository issueRepository;

    public void saveIssue(IssueModel issue) {
        issue.setReportedDate(LocalDate.now()); // Set tanggal otomatis
        issue.setStatus("new"); // Set status otomatis
        issueRepository.save(issue);
    }

    public List<IssueModel> getIssuesByRoom(RoomModel room) {
        return issueRepository.findByRoom(room);
    }
}

4. Controller untuk Form Issue:
@Controller
public class IssueController {

    @Autowired
    private IssueService issueService;

    @Autowired
    private RoomService roomService;

    // Menampilkan halaman detail ruangan beserta daftar masalah
    @GetMapping("/ruang/{ruangId}/view")
    public String viewRoomIssues(@PathVariable Long ruangId, Model model) {
        RoomModel room = roomService.getRoomById(ruangId);
        List<IssueModel> issues = issueService.getIssuesByRoom(room);
        
        model.addAttribute("room", room);
        model.addAttribute("issues", issues);
        model.addAttribute("issue", new IssueModel());
        return "room/detail";
    }

    // Menangani form submit untuk menambahkan masalah barang rusak
    @PostMapping("/ruang/{ruangId}/view")
    public String addIssue(@PathVariable Long ruangId, @ModelAttribute IssueModel issue, RedirectAttributes redirectAttributes) {
        RoomModel room = roomService.getRoomById(ruangId);
        issue.setRoom(room);
        issueService.saveIssue(issue);
        redirectAttributes.addFlashAttribute("success", "Laporan masalah berhasil disimpan.");
        return "redirect:/ruang/" + ruangId + "/view";
    }
}

5. Form, buat form di halaman detail ruangan untuk menambahkan laporan masalah:
<!DOCTYPE html>
<html lang="en" xmlns:th="http://thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>Detail Ruangan</title>
    <object th:insert="~{fragments/fragment::css}" th:remove="tag"></object>
    <object th:insert="~{fragments/fragment::js}" th:remove="tag"></object>
</head>
<body>
<nav th:replace="~{fragments/fragment::navbar(~{::home})}"></nav>

<div class="container">
    <h1>Detail Ruangan</h1>

    <p>Nomor Ruangan: <span th:text="${room.roomNumber}"></span></p>
    <p>Nama Ruangan: <span th:text="${room.roomName}"></span></p>
    <p>Nama Gedung: <span th:text="${room.buildingName}"></span></p>
    <p>Fakultas: <span th:text="${room.faculty}"></span></p>

    <h2>Daftar Masalah</h2>
    <table class="table">
        <thead>
            <tr>
                <th>Deskripsi</th>
                <th>Tanggal Dilaporkan</th>
                <th>Pelapor</th>
                <th>Status</th>
            </tr>
        </thead>
        <tbody>
            <tr th:each="issue : ${issues}">
                <td th:text="${issue.description}"></td>
                <td th:text="${issue.reportedDate}"></td>
                <td th:text="${issue.reporter}"></td>
                <td th:text="${issue.status}"></td>
            </tr>
        </tbody>
    </table>

    <h2>Buat Laporan Masalah</h2>
    <form action="#" th:action="@{/ruang/{ruangId}/view(ruangId=${room.roomId})}" th:object="${issue}" method="post">
        <div class="form-group">
            <label for="description">Deskripsi Masalah</label>
            <input type="text" class="form-control" id="description" th:field="*{description}" placeholder="Masukkan deskripsi masalah" required>
        </div>

        <div class="form-group">
            <label for="reporter">Pelapor</label>
            <input type="text" class="form-control" id="reporter" th:field="*{reporter}" placeholder="Masukkan nama pelapor" required>
        </div>

        <button type="submit" class="btn btn-primary">Submit</button>
    </form>
</div>

</body>
</html>
URL mapping: http://<domain_name>:8080/ruang/{ruangId}/view

---------------------------------------------
Fitur #4 Update Status Masalah:
1. Edit issue controller
@Controller
public class IssueController {

    @Autowired
    private IssueService issueService;

    // Menampilkan form untuk update status
    @GetMapping("/lapor/{issueId}/update")
    public String showUpdateStatusForm(@PathVariable Long issueId, Model model) {
        IssueModel issue = issueService.getIssueById(issueId);
        model.addAttribute("issue", issue);
        return "issue/update-status";  // Menuju ke halaman update status
    }

    // Menangani submit form update status
    @PostMapping("/lapor/{issueId}/update")
    public String updateIssueStatus(@PathVariable Long issueId, @ModelAttribute IssueModel issue, RedirectAttributes redirectAttributes) {
        IssueModel existingIssue = issueService.getIssueById(issueId);
        existingIssue.setStatus(issue.getStatus());  // Update status baru
        issueService.saveIssue(existingIssue);
        redirectAttributes.addFlashAttribute("success", "Status berhasil diubah.");
        return "redirect:/ruang/" + existingIssue.getRoom().getRoomId() + "/view";
    }
}

2. Form di thymeleaf: update-status
<!DOCTYPE html>
<html lang="en" xmlns:th="http://thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>Update Status Masalah</title>
    <object th:insert="~{fragments/fragment::css}" th:remove="tag"></object>
    <object th:insert="~{fragments/fragment::js}" th:remove="tag"></object>
</head>
<body>
<nav th:replace="~{fragments/fragment::navbar(~{::home})}"></nav>

<div class="container">
    <h1>Update Status Masalah</h1>

    <form action="#" th:action="@{/lapor/{issueId}/update(issueId=${issue.issueId})}" th:object="${issue}" method="post">
        <div class="form-group">
            <label for="description">Deskripsi</label>
            <input type="text" class="form-control" id="description" th:field="*{description}" readonly>
        </div>

        <div class="form-group">
            <label for="reporter">Pelapor</label>
            <input type="text" class="form-control" id="reporter" th:field="*{reporter}" readonly>
        </div>

        <div class="form-group">
            <label for="status">Status</label>
            <select class="form-control" id="status" th:field="*{status}">
                <option value="new" th:selected="${issue.status == 'new'}">New</option>
                <option value="in progress" th:selected="${issue.status == 'in progress'}">In Progress</option>
                <option value="done" th:selected="${issue.status == 'done'}">Done</option>
            </select>
        </div>

        <button type="submit" class="btn btn-primary">Submit</button>
        <a href="#" th:href="@{/ruang/{roomId}/view(roomId=${issue.room.roomId})}" class="btn btn-secondary">Back</a>
    </form>
</div>

</body>
</html>

3. Lengkapi issue service
@Service
public class IssueService {

    @Autowired
    private IssueRepository issueRepository;

    public IssueModel getIssueById(Long issueId) {
        return issueRepository.findById(issueId).orElseThrow(() -> new IllegalArgumentException("Issue not found"));
    }

    public void saveIssue(IssueModel issue) {
        issueRepository.save(issue);
    }
}

4. buat repository
public interface IssueRepository extends JpaRepository<IssueModel, Long> {
}

5. flash messages di html: 
<div class="alert alert-success" th:text="${success}" th:if="${success}"></div>
URL mapping: http://localhost:8080/lapor/{issueId}/update

--------------------------------------------

Fitur #5:– Cari Ruangan menggunakan Nama Ruangan

1. Controller buat ruangan (untuk melakukan pencarian):
@Controller
public class RoomController {

    @Autowired
    private RoomService roomService;

    // Menampilkan halaman view all dengan search parameter
    @GetMapping("/ruang/view-all")
    public String viewAllRooms(@RequestParam(value = "search", required = false) String search, Model model) {
        List<RoomModel> rooms;
        if (search != null && !search.isEmpty()) {
            rooms = roomService.searchRoomsByName(search);  // Panggil service untuk mencari ruangan
        } else {
            rooms = roomService.getAllRooms();  // Jika tidak ada pencarian, tampilkan semua ruangan
        }
        model.addAttribute("rooms", rooms);
        model.addAttribute("search", search);  // Menyimpan kata pencarian di model untuk ditampilkan kembali
        return "room/view-all";  // Menuju ke halaman view all rooms
    }
}

2. service untuk melakukan pencarian
@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;

    public List<RoomModel> getAllRooms() {
        return roomRepository.findAll();
    }

    public List<RoomModel> searchRoomsByName(String name) {
        return roomRepository.findByRoomNameContainingIgnoreCase(name);
    }
}

3. tambahkan repositori
public interface RoomRepository extends JpaRepository<RoomModel, Long> {
    List<RoomModel> findByRoomNameContainingIgnoreCase(String roomName);
}

4. tambahkan search form di html search
<!DOCTYPE html>
<html lang="en" xmlns:th="http://thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>Sistem Ruangan</title>
    <object th:insert="~{fragments/fragment::css}" th:remove="tag"></object>
    <object th:insert="~{fragments/fragment::js}" th:remove="tag"></object>
</head>
<body>
<nav th:replace="~{fragments/fragment::navbar(~{::home})}"></nav>

<div class="container">
    <h1>Daftar Ruangan</h1>

    <!-- Form Pencarian -->
    <form action="#" method="get">
        <div class="form-inline">
            <input type="text" name="search" class="form-control" placeholder="Cari Ruangan" th:value="${search}">
            <button type="submit" class="btn btn-primary">Cari Ruangan</button>
        </div>
    </form>

    <table class="table">
        <thead>
            <tr>
                <th>No</th>
                <th>Room Number</th>
                <th>Room Name</th>
                <th>Building Name</th>
                <th>Faculty</th>
                <th>Action</th>
            </tr>
        </thead>
        <tbody>
            <tr th:each="room, iter : ${rooms}">
                <td th:text="${iter.count}"></td>
                <td th:text="${room.roomNumber}"></td>
                <td th:text="${room.roomName}"></td>
                <td th:text="${room.buildingName}"></td>
                <td th:text="${room.faculty}"></td>
                <td>
                    <a th:href="@{/ruang/detail/{id}(id=${room.roomId})}" class="btn btn-primary">Detail</a>
                </td>
            </tr>
        </tbody>
    </table>
</div>

</body>
</html>

URL = http://localhost:8080/ruang/view-all?search=Komputer

--------------------------------------------
Fitur #6: Menghitung Interval Hari sejak Tanggal Dilaporkan
1. add issue model
import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "issue")
public class IssueModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "issue_id")
    private Long issueId;

    @Column(name = "description")
    private String description;

    @Column(name = "reported_date")
    private LocalDate reportedDate;

    @Column(name = "reporter")
    private String reporter;

    @Column(name = "status")
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", referencedColumnName = "room_id")
    private RoomModel room;

    // Constructor kosong
    public IssueModel() {
        this.reportedDate = LocalDate.now(); // Default tanggal saat issue dilaporkan
        this.status = "new";  // Default status adalah 'new'
    }

    // Getter dan Setter untuk semua field
    public Long getIssueId() {
        return issueId;
    }

    public void setIssueId(Long issueId) {
        this.issueId = issueId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getReportedDate() {
        return reportedDate;
    }

    public void setReportedDate(LocalDate reportedDate) {
        this.reportedDate = reportedDate;
    }

    public String getReporter() {
        return reporter;
    }

    public void setReporter(String reporter) {
        this.reporter = reporter;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public RoomModel getRoom() {
        return room;
    }

    public void setRoom(RoomModel room) {
        this.room = room;
    }

    // Transient field untuk menghitung jumlah hari tidak terselesaikan
    @Transient
    public long getUnresolvedDays() {
        if (this.reportedDate != null) {
            return ChronoUnit.DAYS.between(this.reportedDate, LocalDate.now());
        }
        return 0;
    }
}

2. add controller
@Controller
public class ReportController {

    @Autowired
    private IssueService issueService;

    // Menampilkan daftar masalah aktif
    @GetMapping("/report/active")
    public String viewActiveIssues(Model model) {
        List<IssueModel> activeIssues = issueService.getActiveIssues();
        model.addAttribute("issues", activeIssues);
        return "report/active-issues";  // Menuju ke halaman view active issues
    }
}


3.  Service untuk Mengambil Data Masalah Aktif
@Service
public class IssueService {

    @Autowired
    private IssueRepository issueRepository;

    public List<IssueModel> getActiveIssues() {
        return issueRepository.findByStatusIn(Arrays.asList("new", "in progress"));
    }
}

4. repository untuk masalah aktif
public interface IssueRepository extends JpaRepository<IssueModel, Long> {
    List<IssueModel> findByStatusIn(List<String> statuses);
}

5. view di thymeleaf: active-issues
<!DOCTYPE html>
<html lang="en" xmlns:th="http://thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>Masalah Aktif</title>
    <object th:insert="~{fragments/fragment::css}" th:remove="tag"></object>
    <object th:insert="~{fragments/fragment::js}" th:remove="tag"></object>
</head>
<body>
<nav th:replace="~{fragments/fragment::navbar(~{::home})}"></nav>

<div class="container">
    <h1>Daftar Masalah Aktif</h1>
    
    <table class="table">
        <thead>
            <tr>
                <th>Issue Id</th>
                <th>Room Name</th>
                <th>Description</th>
                <th>Reported On</th>
                <th>Status</th>
                <th>Unresolved Days</th>
            </tr>
        </thead>
        <tbody>
            <tr th:each="issue : ${issues}">
                <td th:text="${issue.issueId}"></td>
                <td th:text="${issue.room.roomName}"></td>
                <td th:text="${issue.description}"></td>
                <td th:text="${issue.reportedDate}"></td>
                <td th:text="${issue.status}"></td>
                <td th:text="${issue.unresolvedDays}"></td>
            </tr>
        </tbody>
    </table>
</div>

</body>
</html>

URL: http://localhost:8080/report/active
----------------------------------------------
Fitur #7: Daftar Masalah Aktif Menggunakan Datatables 

URL mapping: http://localhost:8080/report/active
REST API mapping: http://localhost:8080/api/lapor/active

1. Controller untuk REST API
Kamu perlu membuat controller khusus yang menyediakan data masalah aktif dalam format JSON.

java
Salin kode
@RestController
@RequestMapping("/api/lapor")
public class ReportRestController {

    @Autowired
    private IssueService issueService;

    @GetMapping("/active")
    public List<IssueModel> getActiveIssues() {
        return issueService.getActiveIssues();  // Mengembalikan daftar masalah aktif
    }
}
2. Service untuk Mengambil Data Masalah Aktif
Service ini akan mengambil data masalah yang statusnya "new" atau "in progress".

java
Salin kode
@Service
public class IssueService {

    @Autowired
    private IssueRepository issueRepository;

    public List<IssueModel> getActiveIssues() {
        return issueRepository.findByStatusIn(Arrays.asList("new", "in progress"));
    }
}
3. Repository untuk Mengambil Data
Tambahkan query repository untuk mengambil masalah aktif berdasarkan status:

java
Salin kode
public interface IssueRepository extends JpaRepository<IssueModel, Long> {
    List<IssueModel> findByStatusIn(List<String> statuses);
}
4. Konfigurasi JSON Response
Pastikan IssueModel di-serialize dengan benar ke format JSON, dan informasi yang diinginkan (seperti unresolvedDays) juga dikirimkan.
import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "issue")
public class IssueModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "issue_id")
    private Long issueId;

    @Column(name = "description")
    private String description;

    @Column(name = "reported_date")
    private LocalDate reportedDate;

    @Column(name = "reporter")
    private String reporter;

    @Column(name = "status")
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", referencedColumnName = "room_id")
    private RoomModel room;

    // Constructor kosong
    public IssueModel() {
        this.reportedDate = LocalDate.now(); // Set tanggal otomatis saat issue dilaporkan
        this.status = "new"; // Status default adalah 'new'
    }

    // Getter dan Setter untuk semua field
    public Long getIssueId() {
        return issueId;
    }

    public void setIssueId(Long issueId) {
        this.issueId = issueId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getReportedDate() {
        return reportedDate;
    }

    public void setReportedDate(LocalDate reportedDate) {
        this.reportedDate = reportedDate;
    }

    public String getReporter() {
        return reporter;
    }

    public void setReporter(String reporter) {
        this.reporter = reporter;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public RoomModel getRoom() {
        return room;
    }

    public void setRoom(RoomModel room) {
        this.room = room;
    }

    // Transient field untuk menghitung jumlah hari tidak terselesaikan
    @Transient
    public long getUnresolvedDays() {
        if (this.reportedDate != null) {
            return ChronoUnit.DAYS.between(this.reportedDate, LocalDate.now());
        }
        return 0;
    }
}

5. Response API
Ketika kamu mengakses URL seperti ini:

bash
Salin kode
http://localhost:8080/api/lapor/active
Kamu akan mendapatkan respons JSON dengan format seperti ini:

json
Salin kode
[
    {
        "issueId": 1005,
        "description": "Mic Dosen Rusak Semua",
        "reporter": "Susi",
        "reportedDate": "2023-10-17",
        "status": "new",
        "room": {
            "roomId": 1,
            "roomNumber": "B.1101",
            "roomName": "Lab Bahasa 1",
            "buildingName": "Gedung B",
            "faculty": "FIB"
        },
        "unresolvedDays": 0
    },
    {
        "issueId": 1002,
        "description": "Meja Dosen Rusak",
        "reporter": "Budi",
        "reportedDate": "2023-10-10",
        "status": "in progress",
        "room": {
            "roomId": 2,
            "roomNumber": "A.1101",
            "roomName": "Lab Komputer 1",
            "buildingName": "Gedung A",
            "faculty": "FASILKOM"
        },
        "unresolvedDays": 7
    }
]

---------------------------------------------


Fitur #8 dan Fitur #9 (Statistik Jumlah Kasus Aktif per Fakultas) dengan datatables

### 1. **REST API untuk Statistik Kasus Aktif per Fakultas**
   Pertama, kamu perlu membuat REST API yang mengembalikan jumlah kasus aktif per fakultas dalam format JSON.

#### **Controller untuk REST API**
```java
@RestController
@RequestMapping("/api/lapor")
public class ReportRestController {

    @Autowired
    private IssueService issueService;

    // Mengembalikan statistik kasus aktif per fakultas
    @GetMapping("/statistics")
    public List<StatisticsResponse> getStatistics() {
        return issueService.getStatisticsByFaculty();
    }
}
```

#### **DTO untuk Response Statistik**
Buatlah DTO (Data Transfer Object) untuk mengembalikan data statistik.

```java
public class StatisticsResponse {
    private String fakultas;
    private long jumlahKasus;

    // Constructor, Getter, and Setter
    public StatisticsResponse(String fakultas, long jumlahKasus) {
        this.fakultas = fakultas;
        this.jumlahKasus = jumlahKasus;
    }

    public String getFakultas() {
        return fakultas;
    }

    public void setFakultas(String fakultas) {
        this.fakultas = fakultas;
    }

    public long getJumlahKasus() {
        return jumlahKasus;
    }

    public void setJumlahKasus(long jumlahKasus) {
        this.jumlahKasus = jumlahKasus;
    }
}
```

#### **Service untuk Mengambil Statistik Kasus Aktif per Fakultas**
Di service, tambahkan logika untuk menghitung jumlah kasus aktif (status "new" dan "in progress") per fakultas.

```java
@Service
public class IssueService {

    @Autowired
    private IssueRepository issueRepository;

    // Mendapatkan statistik jumlah kasus aktif per fakultas
    public List<StatisticsResponse> getStatisticsByFaculty() {
        List<Object[]> results = issueRepository.countActiveIssuesByFaculty();
        return results.stream()
                .map(result -> new StatisticsResponse((String) result[0], (Long) result[1]))
                .collect(Collectors.toList());
    }
}
```

#### **Repository untuk Menghitung Jumlah Kasus Aktif**
Tambahkan query di repository untuk menghitung jumlah kasus aktif berdasarkan fakultas.

```java
public interface IssueRepository extends JpaRepository<IssueModel, Long> {

    @Query("SELECT r.faculty, COUNT(i) FROM IssueModel i JOIN i.room r WHERE i.status IN ('new', 'in progress') GROUP BY r.faculty")
    List<Object[]> countActiveIssuesByFaculty();
}
```

### 2. **View di Thymeleaf untuk Statistik**
Buat template HTML yang akan menampilkan data dalam bentuk tabel menggunakan DataTables dan grafik batang menggunakan Chart.js.

#### **HTML untuk Statistik (statistics.html)**
```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>Statistik Kasus Aktif per Fakultas</title>
    <link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/1.11.3/css/jquery.dataTables.css">
    <script src="https://code.jquery.com/jquery-3.5.1.js"></script>
    <script type="text/javascript" charset="utf8" src="https://cdn.datatables.net/1.11.3/js/jquery.dataTables.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
</head>
<body>
<nav th:replace="~{fragments/fragment::navbar(~{::home})}"></nav>

<div class="container">
    <h1>Statistik Kasus Aktif per Fakultas</h1>

    <!-- Tabel Statistik -->
    <table id="statisticsTable" class="display">
        <thead>
            <tr>
                <th>Fakultas</th>
                <th>Jumlah Kasus</th>
            </tr>
        </thead>
        <tbody>
            <!-- Data akan diisi oleh JavaScript -->
        </tbody>
    </table>

    <!-- Chart Statistik -->
    <h2>Chart</h2>
    <canvas id="statisticsChart" width="400" height="200"></canvas>
</div>

<script>
    // Menggunakan AJAX untuk mendapatkan data dari REST API
    $(document).ready(function() {
        $.ajax({
            url: '/api/lapor/statistics',
            method: 'GET',
            success: function(data) {
                // Isi tabel DataTables
                var table = $('#statisticsTable').DataTable();
                data.forEach(function(item) {
                    table.row.add([item.fakultas, item.jumlahKasus]).draw();
                });

                // Membuat Chart.js
                var ctx = document.getElementById('statisticsChart').getContext('2d');
                var chart = new Chart(ctx, {
                    type: 'bar',
                    data: {
                        labels: data.map(item => item.fakultas),
                        datasets: [{
                            label: 'Jumlah Kasus Aktif',
                            data: data.map(item => item.jumlahKasus),
                            backgroundColor: 'rgba(75, 192, 192, 0.2)',
                            borderColor: 'rgba(75, 192, 192, 1)',
                            borderWidth: 1
                        }]
                    },
                    options: {
                        scales: {
                            y: {
                                beginAtZero: true
                            }
                        }
                    }
                });
            }
        });
    });
</script>

</body>
</html>
```

### 3. **Library yang Digunakan**
- **DataTables**: Library ini akan membuat tabel lebih interaktif dengan sorting, searching, dan pagination.
- **Chart.js**: Digunakan untuk menampilkan data dalam bentuk chart atau grafik batang.

### 4. **URL Mapping**
- Untuk mengakses halaman statistik, kamu bisa menggunakan URL:
  ```
  http://localhost:8080/report/statistics
  ```
- REST API dapat diakses dengan URL:
  ```
  http://localhost:8080/api/lapor/statistics
  ```

### Penjelasan:
1. **REST API**: Mengembalikan data statistik jumlah kasus aktif per fakultas.
2. **DataTables**: Tabel akan otomatis diisi dengan data statistik dari REST API menggunakan JavaScript.
3. **Chart.js**: Data yang sama digunakan untuk membuat grafik batang yang menunjukkan jumlah kasus aktif per fakultas.

---------------------------------------------
### Fitur #10 – Mengubah Setter/Field Injection menjadi Constructor Dependency Injection

**Dependency Injection (DI)** adalah pola desain di mana objek disediakan oleh framework (seperti Spring) alih-alih objek membuat dependensinya sendiri. Menggunakan **Constructor Injection** lebih dianjurkan daripada **Setter/Field Injection** karena lebih aman dan memungkinkan objek menjadi immutable setelah dibuat. Berikut adalah langkah-langkah untuk mengubah Setter/Field Injection ke Constructor Injection.

#### **Contoh Sebelum: Field Injection atau Setter Injection**
Misalnya kamu memiliki `IssueService` yang di-inject menggunakan Field Injection atau Setter Injection:

**Field Injection:**
```java
@Service
public class IssueService {

    @Autowired
    private IssueRepository issueRepository;

}
```

**Setter Injection:**
```java
@Service
public class IssueService {

    private IssueRepository issueRepository;

    @Autowired
    public void setIssueRepository(IssueRepository issueRepository) {
        this.issueRepository = issueRepository;
    }

    // Logika lainnya...
}
```

#### **Mengubah Menjadi Constructor Injection**
Untuk mengubahnya menjadi **Constructor Injection**, hapus anotasi `@Autowired` pada field atau setter, dan injeksikan dependensi melalui constructor:

```java
@Service
public class IssueService {

    private final IssueRepository issueRepository;

    // Constructor Injection
    public IssueService(IssueRepository issueRepository) {
        this.issueRepository = issueRepository;
    }

    // Logika lainnya...
}
```

### Mengapa Constructor Injection Lebih Baik?
1. **Immutability**: Field yang di-inject melalui constructor dapat dibuat `final`, sehingga tidak bisa berubah setelah objek dibuat.
2. **Testability**: Constructor Injection lebih mudah untuk diujikan karena kamu bisa dengan mudah membuat instance menggunakan mock tanpa memerlukan Spring Context.
3. **Null Safety**: Dengan Constructor Injection, kamu dapat memaksa dependensi disediakan pada saat pembuatan objek, sehingga menghindari `NullPointerException`.

