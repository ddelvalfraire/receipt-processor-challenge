# Receipt Processor

This is a REST API service that processes receipts and calculates points based on specific rules.

## Getting Started

These instructions will help you build and run the application using Docker.

### Prerequisites

- Docker installed on your machine
- Git (to clone the repository)

### Building the Application

1. Clone the repository:
```bash
git clone https://github.com/ddelvalfraire/receipt-processor-challenge.git
cd receipt-processor-challenge
```

2. Build the Docker image:
```bash
docker build -t receipt-processor .
```

### Running the Application

Run the container:
```bash
docker run -p 8080:8080 receipt-processor
```

The application will be available at `http://localhost:8080`

## API Endpoints

- `POST /receipts/process` - Submit a receipt for processing
- `GET /receipts/{id}/points` - Get points for a specific receipt

