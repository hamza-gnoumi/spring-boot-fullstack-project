import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CustomerRegistrationRequest } from 'src/app/models/customer-registration-request';

@Component({
  selector: 'app-manage-customer',
  templateUrl: './manage-customer.component.html',
  styleUrls: ['./manage-customer.component.scss']
})
export class ManageCustomerComponent {



  @Input()
  customer: CustomerRegistrationRequest = {};

  @Output()
  submit: EventEmitter<CustomerRegistrationRequest> =
    new EventEmitter<CustomerRegistrationRequest>();
  @Output()
  cancel: EventEmitter<void> =
    new EventEmitter<void>();

  @Input()
  operation: 'create' | 'update' = 'create';

  title = 'New Customer';

  get isCustomerValid(): boolean {
    return this.hasLength(this.customer.name) &&
      this.hasLength(this.customer.email) &&
      this.customer.age !== undefined && this.customer.age > 0 &&
      (
        this.operation === 'update' ||
        this.hasLength(this.customer.password) &&
        this.hasLength(this.customer.gender)
      )


  }
  private hasLength(input: string | undefined): boolean {
    return input !== null && input !== undefined
      && input.length > 0;
  }

  onSubmit() {
    this.submit.emit(this.customer);
  }
  onCancel() {
    this.cancel.emit();
  }
}
