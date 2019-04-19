import axios from "axios";

export default axios.create({
  baseURL: "http://localhost:8081/",
  withCredentials: true,
  headers: {
    "Content-type": "multipart/form-data",
  }
});
