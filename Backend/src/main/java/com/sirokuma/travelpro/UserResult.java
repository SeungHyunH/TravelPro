@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResult {

    private Long id;
    private String username;
    private String password;

    public UserResult(UserEntity users) {
        this.id = users.getId();
        this.username = users.getUsername();
        this.password = users.getPassword();
    }
}
