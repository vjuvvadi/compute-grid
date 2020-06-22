#compute-grid


@RestController
public class GreetingController {

	public static class Greeting {
		private final String msg;
		private final LocalDate busDate;

		@JsonCreator
		public Greeting(@JsonProperty("msg") String msg,
						@JsonProperty("busDate") LocalDate busDate) {
			this.msg = msg;
			this.busDate = busDate;
		}

		public String getMsg() {
			return msg;
		}

		public LocalDate getBusDate() {
			return busDate;
		}
	}

	@GetMapping("/greeting")
	public Greeting greeting() {
		return new Greeting("Hello", LocalDate.now());
	}

	public void test() {
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<Greeting> entity = restTemplate.getForEntity("http://localhost:8080/greeting", Greeting.class);
		/**
		 *{"msg":"Hello","busDate":"2020-06-22"}
		 */
	}
}
