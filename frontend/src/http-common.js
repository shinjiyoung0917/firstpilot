import axios from "axios";

export default axios.create({
  baseURL: "http://localhost:8081/",
<<<<<<< HEAD
=======
  withCredentials: true,
>>>>>>> 3c2c93843f40de0a2904254b047576ff12a55626
  headers: {
    "Content-type": "application/json",
  }
});
