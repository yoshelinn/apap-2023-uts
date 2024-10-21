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
Fitur #4 Update Status Masalah
