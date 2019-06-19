public interface UserService {
    public User getUserById(int userId);
    public int createUser(int userId,String name,String email);
}