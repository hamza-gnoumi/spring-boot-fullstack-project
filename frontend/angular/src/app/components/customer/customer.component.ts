import { Component, OnInit } from '@angular/core';
import { ConfirmEventType, ConfirmationService, MessageService } from 'primeng/api';
import { CustomerDTO } from 'src/app/models/customer-dto';
import { CustomerRegistrationRequest } from 'src/app/models/customer-registration-request';
import { CustomerService } from 'src/app/services/customer/customer.service';

@Component({
  selector: 'app-customer',
  templateUrl: './customer.component.html',
  styleUrls: ['./customer.component.scss']
})
export class CustomerComponent implements OnInit {





  display = false;
  operation: 'update' | 'create' = 'create';
  customers;
  customer: CustomerRegistrationRequest = {};

  constructor(private confirmationService: ConfirmationService, private customerService: CustomerService,
    private messageService: MessageService) { }
  ngOnInit(): void {
    this.findAllCustomers();
  }
  private findAllCustomers() {
    this.customerService.findAll()
      .subscribe({
        next: (data) => {
          this.customers = data;
        }
      })
  }
  createCustomer() {
    this.display = true;
    this.customer = {};
    this.operation = 'create';
  }

  save(customer: CustomerRegistrationRequest) {
    if (this.operation === 'create') {
      this.customerService.registerCustomer(customer).subscribe({
        next: () => {
          this.findAllCustomers();
          this.display = false;
          this.customer = {};
          this.messageService.add({
            severity: 'success',
            summary: 'Customer saved',
            detail: `Customer ${customer.name} was successfully saved`
          });

        }
      });
    } else {
      this.customerService.updateCustomer(customer.id, customer)
        .subscribe({
          next: () => {
            this.findAllCustomers();
            this.display = false;
            this.customer = {};
            this.messageService.add({
              severity: 'success',
              summary: 'Customer updated',
              detail: `Customer ${customer.name} was successfully updated`
            });
          }
        })
    }

  }
  deleteCustomer(customer: CustomerDTO) {
    this.confirmationService.confirm({
      header: 'Delete Customer',
      message: `Are you sure you wnat to delete ${this.customer.name}? You can't undo this afterwards.`,
      accept: () => {
        this.customerService.deleteCustomer(customer.id)
          .subscribe({
            next: () => {
              this.findAllCustomers();
              this.messageService.add({
                severity: 'success',
                summary: 'Customer deleted',
                detail: `Customer ${customer.name} was successfully deleted`
              });
            }
          });
      },
      reject: (type) => {
        switch (type) {
          case ConfirmEventType.REJECT:
            this.messageService.add({ severity: 'error', summary: 'Rejected', detail: 'You have rejected' });
            break;
          case ConfirmEventType.CANCEL:
            this.messageService.add({ severity: 'warn', summary: 'Cancelled', detail: 'You have cancelled' });
            break;
        }
      }
    });

  }
  updateCustomer(customerDTO: CustomerDTO) {
    this.display = true;
    this.customer = customerDTO;
    this.operation = 'update'
  }
  cancel($event: void) {
    this.display = false;
    this.customer = {};
  }
}
