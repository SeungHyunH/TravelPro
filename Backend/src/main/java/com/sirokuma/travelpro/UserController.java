@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity create(@RequestBody UserDto userDto) {
        log.info("create Users");
        UserEntity users = userService.create(userDto);
        return ResponseEntity.ok(new UserResult(users));
    }

    @GetMapping("{id}")
    public ResponseEntity getUser(@PathVariable Long id) {
        log.info("readOne");
        UserEntity users = userService.readOne(id);
        return ResponseEntity.ok(new UserResult(users));
    }


}
