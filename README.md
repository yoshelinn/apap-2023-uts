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